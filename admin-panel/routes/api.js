const express = require('express');
const router = express.Router();
const {
    AppVersion, CollegeItem, Faculty, Department, Teacher,
    Council, CouncilMember, PrlYear, Photo, ContactInfo,
    ImportantLink, PhoneItem,
} = require('../models');

// GET /api/version
router.get('/version', async (req, res) => {
    try {
        const ver = await AppVersion.findOne();
        res.json({ version: ver ? ver.version : 0 });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// GET /api/data — full AppData JSON
router.get('/data', async (req, res) => {
    try {
        const college = await CollegeItem.findAll({ order: [['sortOrder', 'ASC']] });
        const faculties = await Faculty.findAll({
            include: [{ model: Department, as: 'departments', order: [['sortOrder', 'ASC']] }],
            order: [['sortOrder', 'ASC']],
        });
        const councils = await Council.findAll({
            include: [{ model: CouncilMember, as: 'members', order: [['sortOrder', 'ASC']] }],
            order: [['sortOrder', 'ASC']],
        });
        const teachers = await Teacher.findAll({ where: { isPrl: false }, order: [['serial', 'ASC']] });
        const prl = await PrlYear.findAll({ order: [['sortOrder', 'ASC']] });
        const gallery = await Photo.findAll({ order: [['sortOrder', 'ASC']] });
        const contactRow = await ContactInfo.findOne();
        const importantLinks = await ImportantLink.findAll({ order: [['sortOrder', 'ASC']] });
        const phones = await PhoneItem.findAll({ order: [['sortOrder', 'ASC']] });

        const contact = contactRow ? {
            website: contactRow.website,
            facebook: contactRow.facebook,
            phone: contactRow.phone,
            email: contactRow.email,
            importantLinks: importantLinks.map(l => ({
                title: l.title,
                url: l.url,
                color: l.color,
            })),
        } : null;

        res.json({ college, faculties, councils, teachers, prl, gallery, contact, phones });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// GET /api/college
router.get('/college', async (req, res) => {
    try {
        const items = await CollegeItem.findAll({ order: [['sortOrder', 'ASC']] });
        res.json(items);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// GET /api/departments
router.get('/departments', async (req, res) => {
    try {
        const faculties = await Faculty.findAll({
            include: [{ model: Department, as: 'departments' }],
            order: [['sortOrder', 'ASC']],
        });
        res.json(faculties);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// GET /api/teachers/:deptId
router.get('/teachers/:deptId', async (req, res) => {
    try {
        const teachers = await Teacher.findAll({
            where: { departmentId: req.params.deptId, isPrl: false },
            order: [['serial', 'ASC']],
        });
        res.json(teachers);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// GET /api/council
router.get('/council', async (req, res) => {
    try {
        const councils = await Council.findAll({
            include: [{ model: CouncilMember, as: 'members' }],
            order: [['sortOrder', 'ASC']],
        });
        res.json(councils);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// GET /api/prl
router.get('/prl', async (req, res) => {
    try {
        const years = await PrlYear.findAll({ order: [['sortOrder', 'ASC']] });
        res.json(years);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// GET /api/gallery
router.get('/gallery', async (req, res) => {
    try {
        const photos = await Photo.findAll({ order: [['sortOrder', 'ASC']] });
        res.json(photos);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// GET /api/contact
router.get('/contact', async (req, res) => {
    try {
        const contactRow = await ContactInfo.findOne();
        const importantLinks = await ImportantLink.findAll({ order: [['sortOrder', 'ASC']] });

        const contact = contactRow ? {
            website: contactRow.website,
            facebook: contactRow.facebook,
            phone: contactRow.phone,
            email: contactRow.email,
            importantLinks: importantLinks.map(l => ({
                title: l.title,
                url: l.url,
                color: l.color,
            })),
        } : null;

        res.json(contact);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

module.exports = router;
