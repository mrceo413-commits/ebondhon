/**
 * Seed script — populates the database with sample data.
 * Run: node seed.js
 */
require('dotenv').config();

const {
    sequelize, AppVersion, CollegeItem, Faculty, Department, Teacher,
    Council, CouncilMember, PrlYear, Photo, ContactInfo, ImportantLink, PhoneItem,
} = require('./models');

async function seed() {
    await sequelize.sync({ force: true });
    console.log('Database reset.');

    // Version
    await AppVersion.create({ version: 1 });

    // College items
    await CollegeItem.bulkCreate([
        { id: 'history', title: 'কলেজের ইতিহাস', content: 'কুমিল্লা ভিক্টোরিয়া সরকারি কলেজ ১৮৯৯ সালে প্রতিষ্ঠিত হয়। এটি বাংলাদেশের অন্যতম প্রাচীন ও ঐতিহ্যবাহী শিক্ষা প্রতিষ্ঠান।', type: 'article', sortOrder: 1 },
        { id: 'mission', title: 'মিশন ও ভিশন', content: 'মানসম্মত শিক্ষা প্রদানের মাধ্যমে দক্ষ ও আলোকিত নাগরিক তৈরি করা।', type: 'article', sortOrder: 2 },
        { id: 'principal', title: 'অধ্যক্ষের বাণী', content: 'প্রিয় শিক্ষার্থীবৃন্দ, কুমিল্লা ভিক্টোরিয়া সরকারি কলেজে আপনাদের স্বাগতম।', type: 'article', sortOrder: 3 },
    ]);

    // Faculties & Departments
    const arts = await Faculty.create({ id: 'arts', name: 'কলা অনুষদ', sortOrder: 1 });
    const science = await Faculty.create({ id: 'science', name: 'বিজ্ঞান অনুষদ', sortOrder: 2 });
    const commerce = await Faculty.create({ id: 'commerce', name: 'বাণিজ্য অনুষদ', sortOrder: 3 });

    await Department.bulkCreate([
        { id: 'bangla', name: 'বাংলা', nameEn: 'Bangla', facultyId: 'arts', sortOrder: 1 },
        { id: 'english', name: 'ইংরেজি', nameEn: 'English', facultyId: 'arts', sortOrder: 2 },
        { id: 'history', name: 'ইতিহাস', nameEn: 'History', facultyId: 'arts', sortOrder: 3 },
        { id: 'physics', name: 'পদার্থবিজ্ঞান', nameEn: 'Physics', facultyId: 'science', sortOrder: 1 },
        { id: 'chemistry', name: 'রসায়ন', nameEn: 'Chemistry', facultyId: 'science', sortOrder: 2 },
        { id: 'math', name: 'গণিত', nameEn: 'Mathematics', facultyId: 'science', sortOrder: 3 },
        { id: 'accounting', name: 'হিসাববিজ্ঞান', nameEn: 'Accounting', facultyId: 'commerce', sortOrder: 1 },
        { id: 'management', name: 'ব্যবস্থাপনা', nameEn: 'Management', facultyId: 'commerce', sortOrder: 2 },
    ]);

    // Teachers
    await Teacher.bulkCreate([
        { serial: 1, idNo: 'T001', name: 'ড. মোহাম্মদ আলী', nameEn: 'Dr. Mohammad Ali', designation: 'অধ্যাপক', departmentId: 'physics', birthDate: '01/01/1970', bloodGroup: 'A+', phone: '01711000001', email: 'mali@cvgc.edu.bd' },
        { serial: 2, idNo: 'T002', name: 'প্রফেসর ফাতেমা বেগম', nameEn: 'Prof. Fatema Begum', designation: 'সহযোগী অধ্যাপক', departmentId: 'bangla', birthDate: '15/03/1975', bloodGroup: 'B+', phone: '01711000002', email: 'fbegum@cvgc.edu.bd' },
        { serial: 3, idNo: 'T003', name: 'মোঃ করিম উদ্দিন', nameEn: 'Md. Karim Uddin', designation: 'সহকারী অধ্যাপক', departmentId: 'chemistry', birthDate: '20/06/1980', bloodGroup: 'O+', phone: '01711000003', email: 'karim@cvgc.edu.bd' },
    ]);

    // Councils
    const govBody = await Council.create({ id: 'governing', name: 'গভর্নিং বডি', sortOrder: 1 });
    await CouncilMember.bulkCreate([
        { councilId: 'governing', name: 'জনাব আবদুল হক', designation: 'সভাপতি', sortOrder: 1 },
        { councilId: 'governing', name: 'ড. মোহাম্মদ আলী', designation: 'সদস্য সচিব', sortOrder: 2 },
    ]);

    // PRL Years
    await PrlYear.bulkCreate([
        { year: '2024', yearBn: '২০২৪', sortOrder: 1 },
        { year: '2023', yearBn: '২০২৩', sortOrder: 2 },
        { year: '2022', yearBn: '২০২২', sortOrder: 3 },
    ]);

    // Contact
    await ContactInfo.create({
        website: 'https://www.cvgc.edu.bd',
        facebook: 'https://facebook.com/cvgc.edu.bd',
        phone: '02334405988',
        email: 'principal@cvgc.edu.bd',
    });

    await ImportantLink.bulkCreate([
        { title: 'শিক্ষা মন্ত্রণালয়', url: 'https://moedu.gov.bd', color: '#1A237E', sortOrder: 1 },
        { title: 'মাধ্যমিক ও উচ্চশিক্ষা অধিদপ্তর', url: 'https://dshe.gov.bd', color: '#009688', sortOrder: 2 },
        { title: 'জাতীয় বিশ্ববিদ্যালয়', url: 'https://www.nu.ac.bd', color: '#1A237E', sortOrder: 3 },
    ]);

    // Phone items
    await PhoneItem.bulkCreate([
        { name: 'অধ্যক্ষ', phone: '02334405988', designation: 'অধ্যক্ষ', sortOrder: 1 },
        { name: 'উপাধ্যক্ষ', phone: '02334405989', designation: 'উপাধ্যক্ষ', sortOrder: 2 },
    ]);

    console.log('Seed data created successfully!');
    process.exit(0);
}

seed().catch(err => {
    console.error('Seed failed:', err);
    process.exit(1);
});
