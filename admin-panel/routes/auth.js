const express = require('express');
const router = express.Router();
const bcrypt = require('bcryptjs');

const adminPassHash = bcrypt.hashSync(process.env.ADMIN_PASSWORD || 'admin123', 10);

router.get('/login', (req, res) => {
    if (req.session && req.session.isAdmin) {
        return res.redirect('/admin');
    }
    res.render('pages/login', { layout: false, error: null });
});

router.post('/login', (req, res) => {
    const { username, password } = req.body;
    const adminUser = process.env.ADMIN_USERNAME || 'admin';

    if (username === adminUser && bcrypt.compareSync(password, adminPassHash)) {
        req.session.isAdmin = true;
        return res.redirect('/admin');
    }
    res.render('pages/login', { layout: false, error: 'ভুল ইউজারনেম বা পাসওয়ার্ড' });
});

router.get('/logout', (req, res) => {
    req.session.destroy(() => {
        res.redirect('/login');
    });
});

function requireAuth(req, res, next) {
    if (req.session && req.session.isAdmin) {
        return next();
    }
    res.redirect('/login');
}

module.exports = { router, requireAuth };
