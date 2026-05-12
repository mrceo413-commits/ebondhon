const sequelize = require('../config/database');
const { DataTypes } = require('sequelize');

const AppVersion = sequelize.define('AppVersion', {
    version: { type: DataTypes.INTEGER, allowNull: false, defaultValue: 1 },
}, { tableName: 'app_version' });

const CollegeItem = sequelize.define('CollegeItem', {
    id: { type: DataTypes.STRING, primaryKey: true },
    title: { type: DataTypes.STRING, allowNull: false },
    content: { type: DataTypes.TEXT },
    imageUrl: { type: DataTypes.STRING },
    type: { type: DataTypes.STRING, defaultValue: 'article' },
    sortOrder: { type: DataTypes.INTEGER, defaultValue: 0 },
}, { tableName: 'college_items' });

const Faculty = sequelize.define('Faculty', {
    id: { type: DataTypes.STRING, primaryKey: true },
    name: { type: DataTypes.STRING, allowNull: false },
    sortOrder: { type: DataTypes.INTEGER, defaultValue: 0 },
}, { tableName: 'faculties' });

const Department = sequelize.define('Department', {
    id: { type: DataTypes.STRING, primaryKey: true },
    name: { type: DataTypes.STRING, allowNull: false },
    nameEn: { type: DataTypes.STRING },
    facultyId: { type: DataTypes.STRING, allowNull: false },
    sortOrder: { type: DataTypes.INTEGER, defaultValue: 0 },
}, { tableName: 'departments' });

const Teacher = sequelize.define('Teacher', {
    id: { type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true },
    serial: { type: DataTypes.INTEGER },
    idNo: { type: DataTypes.STRING },
    name: { type: DataTypes.STRING, allowNull: false },
    nameEn: { type: DataTypes.STRING },
    designation: { type: DataTypes.STRING },
    govtJoining: { type: DataTypes.STRING },
    thisCollegeJoining: { type: DataTypes.STRING },
    thisDesignationJoining: { type: DataTypes.STRING },
    birthDate: { type: DataTypes.STRING },
    bloodGroup: { type: DataTypes.STRING },
    addressCurrent: { type: DataTypes.TEXT },
    addressPermanent: { type: DataTypes.TEXT },
    phone: { type: DataTypes.STRING },
    email: { type: DataTypes.STRING },
    facebookLink: { type: DataTypes.STRING },
    photoUrl: { type: DataTypes.STRING },
    departmentId: { type: DataTypes.STRING },
    isPrl: { type: DataTypes.BOOLEAN, defaultValue: false },
    prlYear: { type: DataTypes.STRING },
}, { tableName: 'teachers' });

const Council = sequelize.define('Council', {
    id: { type: DataTypes.STRING, primaryKey: true },
    name: { type: DataTypes.STRING, allowNull: false },
    sortOrder: { type: DataTypes.INTEGER, defaultValue: 0 },
}, { tableName: 'councils' });

const CouncilMember = sequelize.define('CouncilMember', {
    id: { type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true },
    councilId: { type: DataTypes.STRING, allowNull: false },
    name: { type: DataTypes.STRING, allowNull: false },
    designation: { type: DataTypes.STRING },
    photoUrl: { type: DataTypes.STRING },
    sortOrder: { type: DataTypes.INTEGER, defaultValue: 0 },
}, { tableName: 'council_members' });

const PrlYear = sequelize.define('PrlYear', {
    year: { type: DataTypes.STRING, primaryKey: true },
    yearBn: { type: DataTypes.STRING },
    sortOrder: { type: DataTypes.INTEGER, defaultValue: 0 },
}, { tableName: 'prl_years' });

const Photo = sequelize.define('Photo', {
    id: { type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true },
    url: { type: DataTypes.STRING, allowNull: false },
    caption: { type: DataTypes.STRING },
    sortOrder: { type: DataTypes.INTEGER, defaultValue: 0 },
}, { tableName: 'photos' });

const ContactInfo = sequelize.define('ContactInfo', {
    id: { type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true },
    website: { type: DataTypes.STRING },
    facebook: { type: DataTypes.STRING },
    phone: { type: DataTypes.STRING },
    email: { type: DataTypes.STRING },
}, { tableName: 'contact_info' });

const ImportantLink = sequelize.define('ImportantLink', {
    id: { type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true },
    title: { type: DataTypes.STRING, allowNull: false },
    url: { type: DataTypes.STRING, allowNull: false },
    color: { type: DataTypes.STRING, defaultValue: '#1A237E' },
    sortOrder: { type: DataTypes.INTEGER, defaultValue: 0 },
}, { tableName: 'important_links' });

const PhoneItem = sequelize.define('PhoneItem', {
    id: { type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true },
    name: { type: DataTypes.STRING, allowNull: false },
    phone: { type: DataTypes.STRING, allowNull: false },
    designation: { type: DataTypes.STRING },
    sortOrder: { type: DataTypes.INTEGER, defaultValue: 0 },
}, { tableName: 'phone_items' });

// Associations
Faculty.hasMany(Department, { foreignKey: 'facultyId', as: 'departments' });
Department.belongsTo(Faculty, { foreignKey: 'facultyId' });

Council.hasMany(CouncilMember, { foreignKey: 'councilId', as: 'members' });
CouncilMember.belongsTo(Council, { foreignKey: 'councilId' });

module.exports = {
    sequelize,
    AppVersion,
    CollegeItem,
    Faculty,
    Department,
    Teacher,
    Council,
    CouncilMember,
    PrlYear,
    Photo,
    ContactInfo,
    ImportantLink,
    PhoneItem,
};
