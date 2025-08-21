const express = require('express');
const mongoose = require('mongoose');
const jwt = require('jsonwebtoken');
const bodyParser = require('body-parser');
const recordRoutes = require('./routes/record');
const accessRoutes = require('./routes/access');
const authRoutes = require('./routes/auth');

require('dotenv').config();

const app = express();
app.use(bodyParser.json());

mongoose.connect(process.env.MONGO_URI, { useNewUrlParser: true, useUnifiedTopology: true });

// Auth routes
app.use('/api/auth', authRoutes);
// Record upload/fetch
app.use('/api/records', recordRoutes);
// QR/token-based access
app.use('/api/access', accessRoutes);

app.listen(4000, () => console.log('Backend running on port 4000'));
