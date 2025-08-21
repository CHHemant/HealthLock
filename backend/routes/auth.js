const express = require('express');
const router = express.Router();
const jwt = require('jsonwebtoken');

// Dummy user login for demo
router.post('/login', (req, res) => {
    const { userId } = req.body;
    const token = jwt.sign({ userId }, process.env.JWT_SECRET, { expiresIn: '1h' });
    res.json({ token });
});

module.exports = router;
