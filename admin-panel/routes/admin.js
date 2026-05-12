const express = require('express');
const router = express.Router();
const multer = require('multer');
const path = require('path');
const {
    AppVersion, CollegeItem, Faculty, Department, Teacher,
    Council, CouncilMember, PrlYear, Photo, ContactInfo,
    ImportantLink, PhoneItem,
} = require('../models');

// File upload config
const storage = multer.diskStorage({
    destination: (req, file, cb) => cb(null, path.join(__dirname, '..', 'public', 'uploads')),
    filename: (req, file, cb) => {
        const uniqueName = Date.now() + '-' + Math.round(Math.random() * 1E6) + path.extname(file.originalname);
        cb(null, uniqueName);
    },
});
const upload = multer({ storage, limits: { fileSize: 10 * 1024 * 1024 } });

// Helper: bump version
async function bumpVersion() {
    const [ver] = await AppVersion.findOrCreate({ where: {}, defaults: { version: 1 } });
    ver.version += 1;
    await ver.save();
    return ver.version;
}

// Dashboard
router.get('/', async (req, res) => {
    const [teacherCount, deptCount, photoCount, collegeCount] = await Promise.all([
        Teacher.count({ where: { isPrl: false } }),
        Department.count(),
        Photo.count(),
        CollegeItem.count(),
    ]);
    const ver = await AppVersion.findOne();
    res.render('pages/dashboard', {
        title: 'ড্যাশবোর্ড',
        active: 'dashboard',
        teacherCount,
        deptCount,
        photoCount,
        collegeCount,
        version: ver ? ver.version : 0,
    });
});

// === Version ===
router.post('/version/bump', async (req, res) => {
    const newVer = await bumpVersion();
    req.session.flash = { type: 'success', msg: `ভার্সন আপডেট হয়েছে: ${newVer}` };
    res.redirect('/admin');
});

// === College Items ===
router.get('/college', async (req, res) => {
    const items = await CollegeItem.findAll({ order: [['sortOrder', 'ASC']] });
    res.render('pages/college', { title: 'কলেজ তথ্য', active: 'college', items });
});

router.get('/college/add', (req, res) => {
    res.render('pages/college-form', { title: 'নতুন তথ্য যোগ', active: 'college', item: null });
});

router.post('/college/add', upload.single('image'), async (req, res) => {
    const { id, title, content, type, sortOrder } = req.body;
    const imageUrl = req.file ? `/uploads/${req.file.filename}` : req.body.imageUrl || null;
    await CollegeItem.create({ id, title, content, imageUrl, type, sortOrder: parseInt(sortOrder) || 0 });
    await bumpVersion();
    res.redirect('/admin/college');
});

router.get('/college/edit/:id', async (req, res) => {
    const item = await CollegeItem.findByPk(req.params.id);
    if (!item) return res.redirect('/admin/college');
    res.render('pages/college-form', { title: 'সম্পাদনা', active: 'college', item });
});

router.post('/college/edit/:id', upload.single('image'), async (req, res) => {
    const item = await CollegeItem.findByPk(req.params.id);
    if (!item) return res.redirect('/admin/college');
    const { title, content, type, sortOrder } = req.body;
    item.title = title;
    item.content = content;
    item.type = type;
    item.sortOrder = parseInt(sortOrder) || 0;
    if (req.file) item.imageUrl = `/uploads/${req.file.filename}`;
    else item.imageUrl = req.body.imageUrl || null;
    await item.save();
    await bumpVersion();
    res.redirect('/admin/college');
});

router.post('/college/delete/:id', async (req, res) => {
    await CollegeItem.destroy({ where: { id: req.params.id } });
    await bumpVersion();
    res.redirect('/admin/college');
});

// === Faculties & Departments ===
router.get('/faculties', async (req, res) => {
    const faculties = await Faculty.findAll({
        include: [{ model: Department, as: 'departments' }],
        order: [['sortOrder', 'ASC'], [{ model: Department, as: 'departments' }, 'sortOrder', 'ASC']],
    });
    res.render('pages/faculties', { title: 'অনুষদ ও বিভাগ', active: 'faculties', faculties });
});

router.get('/faculties/add', (req, res) => {
    res.render('pages/faculty-form', { title: 'নতুন অনুষদ', active: 'faculties', faculty: null });
});

router.post('/faculties/add', async (req, res) => {
    const { id, name, sortOrder } = req.body;
    await Faculty.create({ id, name, sortOrder: parseInt(sortOrder) || 0 });
    await bumpVersion();
    res.redirect('/admin/faculties');
});

router.get('/faculties/edit/:id', async (req, res) => {
    const faculty = await Faculty.findByPk(req.params.id);
    if (!faculty) return res.redirect('/admin/faculties');
    res.render('pages/faculty-form', { title: 'সম্পাদনা', active: 'faculties', faculty });
});

router.post('/faculties/edit/:id', async (req, res) => {
    const faculty = await Faculty.findByPk(req.params.id);
    if (!faculty) return res.redirect('/admin/faculties');
    const { name, sortOrder } = req.body;
    faculty.name = name;
    faculty.sortOrder = parseInt(sortOrder) || 0;
    await faculty.save();
    await bumpVersion();
    res.redirect('/admin/faculties');
});

router.post('/faculties/delete/:id', async (req, res) => {
    const deptIds = (await Department.findAll({ where: { facultyId: req.params.id }, attributes: ['id'] })).map(d => d.id);
    if (deptIds.length > 0) await Teacher.destroy({ where: { departmentId: deptIds } });
    await Department.destroy({ where: { facultyId: req.params.id } });
    await Faculty.destroy({ where: { id: req.params.id } });
    await bumpVersion();
    res.redirect('/admin/faculties');
});

// Departments
router.get('/departments/add/:facultyId', async (req, res) => {
    const faculty = await Faculty.findByPk(req.params.facultyId);
    res.render('pages/department-form', { title: 'নতুন বিভাগ', active: 'faculties', faculty, dept: null });
});

router.post('/departments/add', async (req, res) => {
    const { id, name, nameEn, facultyId, sortOrder } = req.body;
    await Department.create({ id, name, nameEn, facultyId, sortOrder: parseInt(sortOrder) || 0 });
    await bumpVersion();
    res.redirect('/admin/faculties');
});

router.get('/departments/edit/:id', async (req, res) => {
    const dept = await Department.findByPk(req.params.id);
    if (!dept) return res.redirect('/admin/faculties');
    const faculty = await Faculty.findByPk(dept.facultyId);
    res.render('pages/department-form', { title: 'বিভাগ সম্পাদনা', active: 'faculties', faculty, dept });
});

router.post('/departments/edit/:id', async (req, res) => {
    const dept = await Department.findByPk(req.params.id);
    if (!dept) return res.redirect('/admin/faculties');
    const { name, nameEn, sortOrder } = req.body;
    dept.name = name;
    dept.nameEn = nameEn;
    dept.sortOrder = parseInt(sortOrder) || 0;
    await dept.save();
    await bumpVersion();
    res.redirect('/admin/faculties');
});

router.post('/departments/delete/:id', async (req, res) => {
    await Teacher.destroy({ where: { departmentId: req.params.id } });
    await Department.destroy({ where: { id: req.params.id } });
    await bumpVersion();
    res.redirect('/admin/faculties');
});

// === Teachers ===
router.get('/teachers', async (req, res) => {
    const deptFilter = req.query.dept || '';
    const where = { isPrl: false };
    if (deptFilter) where.departmentId = deptFilter;
    const teachers = await Teacher.findAll({ where, order: [['serial', 'ASC']] });
    const departments = await Department.findAll({ order: [['name', 'ASC']] });
    res.render('pages/teachers', { title: 'শিক্ষকবৃন্দ', active: 'teachers', teachers, departments, deptFilter });
});

router.get('/teachers/add', async (req, res) => {
    const departments = await Department.findAll({ order: [['name', 'ASC']] });
    res.render('pages/teacher-form', { title: 'নতুন শিক্ষক', active: 'teachers', teacher: null, departments });
});

router.post('/teachers/add', upload.single('photo'), async (req, res) => {
    const fields = [
        'idNo', 'name', 'nameEn', 'designation',
        'govtJoining', 'thisCollegeJoining', 'thisDesignationJoining',
        'birthDate', 'bloodGroup', 'addressCurrent', 'addressPermanent',
        'phone', 'email', 'facebookLink', 'departmentId', 'prlYear',
    ];
    const data = {};
    fields.forEach(f => { if (req.body[f] !== undefined) data[f] = req.body[f]; });
    data.photoUrl = req.file ? `/uploads/${req.file.filename}` : req.body.photoUrl || null;
    data.serial = parseInt(req.body.serial) || 0;
    data.isPrl = req.body.isPrl === 'on' || req.body.isPrl === 'true';
    await Teacher.create(data);
    await bumpVersion();
    res.redirect('/admin/teachers');
});

router.get('/teachers/edit/:id', async (req, res) => {
    const teacher = await Teacher.findByPk(req.params.id);
    if (!teacher) return res.redirect('/admin/teachers');
    const departments = await Department.findAll({ order: [['name', 'ASC']] });
    res.render('pages/teacher-form', { title: 'সম্পাদনা', active: 'teachers', teacher, departments });
});

router.post('/teachers/edit/:id', upload.single('photo'), async (req, res) => {
    const teacher = await Teacher.findByPk(req.params.id);
    if (!teacher) return res.redirect('/admin/teachers');

    const fields = [
        'serial', 'idNo', 'name', 'nameEn', 'designation',
        'govtJoining', 'thisCollegeJoining', 'thisDesignationJoining',
        'birthDate', 'bloodGroup', 'addressCurrent', 'addressPermanent',
        'phone', 'email', 'facebookLink', 'departmentId', 'prlYear',
    ];
    fields.forEach(f => { if (req.body[f] !== undefined) teacher[f] = req.body[f]; });
    teacher.serial = parseInt(req.body.serial) || 0;
    teacher.isPrl = req.body.isPrl === 'on' || req.body.isPrl === 'true';
    if (req.file) teacher.photoUrl = `/uploads/${req.file.filename}`;
    else teacher.photoUrl = req.body.photoUrl || null;
    await teacher.save();
    await bumpVersion();
    res.redirect('/admin/teachers');
});

router.post('/teachers/delete/:id', async (req, res) => {
    await Teacher.destroy({ where: { id: req.params.id } });
    await bumpVersion();
    res.redirect('/admin/teachers');
});

// === Council ===
router.get('/councils', async (req, res) => {
    const councils = await Council.findAll({
        include: [{ model: CouncilMember, as: 'members' }],
        order: [['sortOrder', 'ASC'], [{ model: CouncilMember, as: 'members' }, 'sortOrder', 'ASC']],
    });
    res.render('pages/councils', { title: 'পরিষদ', active: 'councils', councils });
});

router.get('/councils/add', (req, res) => {
    res.render('pages/council-form', { title: 'নতুন পরিষদ', active: 'councils', council: null });
});

router.post('/councils/add', async (req, res) => {
    const { id, name, sortOrder } = req.body;
    await Council.create({ id, name, sortOrder: parseInt(sortOrder) || 0 });
    await bumpVersion();
    res.redirect('/admin/councils');
});

router.get('/councils/edit/:id', async (req, res) => {
    const council = await Council.findByPk(req.params.id);
    if (!council) return res.redirect('/admin/councils');
    res.render('pages/council-form', { title: 'পরিষদ সম্পাদনা', active: 'councils', council });
});

router.post('/councils/edit/:id', async (req, res) => {
    const council = await Council.findByPk(req.params.id);
    if (!council) return res.redirect('/admin/councils');
    council.name = req.body.name;
    council.sortOrder = parseInt(req.body.sortOrder) || 0;
    await council.save();
    await bumpVersion();
    res.redirect('/admin/councils');
});

router.post('/councils/delete/:id', async (req, res) => {
    await CouncilMember.destroy({ where: { councilId: req.params.id } });
    await Council.destroy({ where: { id: req.params.id } });
    await bumpVersion();
    res.redirect('/admin/councils');
});

// Council Members
router.get('/council-members/add/:councilId', async (req, res) => {
    const council = await Council.findByPk(req.params.councilId);
    res.render('pages/council-member-form', { title: 'নতুন সদস্য', active: 'councils', council, member: null });
});

router.post('/council-members/add', upload.single('photo'), async (req, res) => {
    const { councilId, name, designation, sortOrder } = req.body;
    const photoUrl = req.file ? `/uploads/${req.file.filename}` : req.body.photoUrl || null;
    await CouncilMember.create({ councilId, name, designation, photoUrl, sortOrder: parseInt(sortOrder) || 0 });
    await bumpVersion();
    res.redirect('/admin/councils');
});

router.post('/council-members/delete/:id', async (req, res) => {
    await CouncilMember.destroy({ where: { id: req.params.id } });
    await bumpVersion();
    res.redirect('/admin/councils');
});

// === PRL ===
router.get('/prl', async (req, res) => {
    const years = await PrlYear.findAll({ order: [['sortOrder', 'ASC']] });
    res.render('pages/prl', { title: 'পি.আর.এল', active: 'prl', years });
});

router.post('/prl/add', async (req, res) => {
    const { year, yearBn, sortOrder } = req.body;
    await PrlYear.findOrCreate({ where: { year }, defaults: { yearBn, sortOrder: parseInt(sortOrder) || 0 } });
    await bumpVersion();
    res.redirect('/admin/prl');
});

router.post('/prl/delete/:year', async (req, res) => {
    await PrlYear.destroy({ where: { year: req.params.year } });
    await bumpVersion();
    res.redirect('/admin/prl');
});

// === Gallery ===
router.get('/gallery', async (req, res) => {
    const photos = await Photo.findAll({ order: [['sortOrder', 'ASC']] });
    res.render('pages/gallery', { title: 'ফটো গ্যালারি', active: 'gallery', photos });
});

router.post('/gallery/add', upload.single('photo'), async (req, res) => {
    const url = req.file ? `/uploads/${req.file.filename}` : req.body.url;
    if (!url) return res.redirect('/admin/gallery');
    const { caption, sortOrder } = req.body;
    await Photo.create({ url, caption, sortOrder: parseInt(sortOrder) || 0 });
    await bumpVersion();
    res.redirect('/admin/gallery');
});

router.post('/gallery/delete/:id', async (req, res) => {
    await Photo.destroy({ where: { id: req.params.id } });
    await bumpVersion();
    res.redirect('/admin/gallery');
});

// === Contact ===
router.get('/contact', async (req, res) => {
    const contact = await ContactInfo.findOne();
    const links = await ImportantLink.findAll({ order: [['sortOrder', 'ASC']] });
    res.render('pages/contact', { title: 'যোগাযোগ', active: 'contact', contact, links });
});

router.post('/contact/save', async (req, res) => {
    const { website, facebook, phone, email } = req.body;
    const [contact] = await ContactInfo.findOrCreate({ where: {}, defaults: { website, facebook, phone, email } });
    contact.website = website;
    contact.facebook = facebook;
    contact.phone = phone;
    contact.email = email;
    await contact.save();
    await bumpVersion();
    res.redirect('/admin/contact');
});

router.post('/contact/links/add', async (req, res) => {
    const { title, url, color, sortOrder } = req.body;
    await ImportantLink.create({ title, url, color: color || '#1A237E', sortOrder: parseInt(sortOrder) || 0 });
    await bumpVersion();
    res.redirect('/admin/contact');
});

router.post('/contact/links/delete/:id', async (req, res) => {
    await ImportantLink.destroy({ where: { id: req.params.id } });
    await bumpVersion();
    res.redirect('/admin/contact');
});

// === Phone Numbers ===
router.get('/phones', async (req, res) => {
    const phones = await PhoneItem.findAll({ order: [['sortOrder', 'ASC']] });
    res.render('pages/phones', { title: 'টেলিফোন নম্বর', active: 'phones', phones });
});

router.post('/phones/add', async (req, res) => {
    const { name, phone, designation, sortOrder } = req.body;
    await PhoneItem.create({ name, phone, designation, sortOrder: parseInt(sortOrder) || 0 });
    await bumpVersion();
    res.redirect('/admin/phones');
});

router.post('/phones/delete/:id', async (req, res) => {
    await PhoneItem.destroy({ where: { id: req.params.id } });
    await bumpVersion();
    res.redirect('/admin/phones');
});

module.exports = router;
