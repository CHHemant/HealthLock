const express = require('express');
const router = express.Router();
const Record = require('../models/Record');
const { encryptFile, decryptFile } = require('../utils/encryption');

// Upload new medical record
router.post('/upload', async (req, res) => {
    // Save encrypted file and metadata
    const { patientId, file, accessLevel } = req.body;
    const encryptedFile = encryptFile(file);
    const record = new Record({ patientId, file: encryptedFile, encrypted: true, accessLevel });
    await record.save();
    res.json({ success: true, id: record._id });
});

// Fetch records for patient
router.get('/:patientId', async (req, res) => {
    const records = await Record.find({ patientId: req.params.patientId });
    res.json(records);
});

module.exports = router;
