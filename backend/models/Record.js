const mongoose = require('mongoose');

const AccessLogSchema = new mongoose.Schema({
    timestamp: Date,
    userId: String,
    action: String // 'view', 'download', 'share'
});

const RecordSchema = new mongoose.Schema({
    patientId: String,
    file: String, // File path or FHIR reference
    encrypted: Boolean,
    accessLevel: { type: String, enum: ['doctor', 'pharmacist', 'diagnostic'] },
    sharedTokens: [{ token: String, expires: Date }],
    accessLogs: [AccessLogSchema]
});

module.exports = mongoose.model('Record', RecordSchema);
