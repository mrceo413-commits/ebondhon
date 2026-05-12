require('dotenv').config();

const express = require('express');
const path = require('path');
const session = require('express-session');
const fs = require('fs');

const { sequelize } = require('./models');
const apiRoutes = require('./routes/api');
const adminRoutes = require('./routes/admin');
const { router: authRouter, requireAuth } = require('./routes/auth');

const app = express();
const PORT = process.env.PORT || 3000;

// Ensure data and uploads directories exist
const dataDir = path.join(__dirname, 'data');
const uploadsDir = path.join(__dirname, 'public', 'uploads');
if (!fs.existsSync(dataDir)) fs.mkdirSync(dataDir, { recursive: true });
if (!fs.existsSync(uploadsDir)) fs.mkdirSync(uploadsDir, { recursive: true });

// EJS view engine with layout support
app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));

// Simple layout middleware
const originalRender = app.response.render;
app.response.render = function(view, options, callback) {
    const self = this;
    const opts = options || {};

    if (opts.layout === false) {
        return originalRender.call(self, view, opts, callback);
    }

    originalRender.call(self, view, opts, function(err, html) {
        if (err) {
            if (callback) return callback(err);
            return self.req.next(err);
        }
        opts.body = html;
        originalRender.call(self, 'layouts/main', opts, callback);
    });
};

// Middleware
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(express.static(path.join(__dirname, 'public')));
app.use(session({
    secret: process.env.SESSION_SECRET || 'ebondhon-secret-key',
    resave: false,
    saveUninitialized: false,
    cookie: { maxAge: 24 * 60 * 60 * 1000 },
}));

// Routes
app.use('/api', apiRoutes);
app.use('/', authRouter);
app.use('/admin', requireAuth, adminRoutes);

// Root redirect
app.get('/', (req, res) => res.redirect('/admin'));

// Start server
async function start() {
    try {
        await sequelize.sync();
        console.log('Database synced');

        // Create initial version record if none exists
        const { AppVersion } = require('./models');
        const [ver] = await AppVersion.findOrCreate({ where: {}, defaults: { version: 1 } });
        console.log(`Current app version: ${ver.version}`);

        app.listen(PORT, () => {
            console.log(`E-Bondhon Admin Panel running at http://localhost:${PORT}`);
            console.log(`API available at http://localhost:${PORT}/api/`);
        });
    } catch (err) {
        console.error('Failed to start:', err);
        process.exit(1);
    }
}

start();
