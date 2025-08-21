const express = require('express');
const router = express.Router();
const Record = require('../models/Record');
const jwt = require('jsonwebtoken');

// Generate QR/token for sharing
router.post('/generate-token', async (req, res) => {
    const { recordId, accessLevel } = req.body;
    const token = jwt.sign({ recordId, accessLevel }, process.env.JWT_SECRET, { expiresIn: '15m' }); // 15 min validity
    await Record.findByIdAndUpdate(recordId, { $push: { sharedTokens: { token, expires: Date.now() + 900000 } } });
    res.json({ token });
});

// Doctor scans QR/token
router.post('/access-with-token', async (req, res) => {
    const { token } = req.body;
    try {
        const payload = jwt.verify(token, process.env.JWT_SECRET);
        const record = await Record.findById(payload.recordId);
        // Log access
        record.accessLogs.push({ timestamp: new Date(), userId: req.user.id, action: 'view' });
        await record.save();
        res.json({ file: record.file, accessLevel: payload.accessLevel });
    } catch (err) {
        res.status(401).json({ error: 'Invalid or expired token' });
    }
});

module.exports = router;
