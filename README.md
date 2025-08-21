# HealthLock – Secure Patient Record Sharing

HealthLock is a secure health tech solution for storing and sharing patient records using QR/token-based access. Patients control who accesses their data, with encryption and role-based permissions for privacy and compliance.

---

## Why I Chose This Project

- Healthcare Data Privacy: Patient records are sensitive and need robust security.
- Ease of Sharing: QR/token-based sharing makes record transfer simple and fast.
- Patient Empowerment: Role-based access ensures patients control who sees what.
- Hackathon Ready: Features are innovative but realistic to build in limited time.

---

## Technologies Used

- Android (Kotlin): For the user-facing mobile app.
- Node.js + Express: Backend API server.
- MongoDB: Secure data storage.
- JWT (JSON Web Token): For generating secure, time-limited access tokens.
- QR Code (ZXing): For scan-based, instant sharing.
- Retrofit: Network/API calls from the Android app.
- End-to-End Encryption: AES-based file encryption.
- Role-Based Access Control (RBAC): Patients grant specific permissions per role.

---

## How It Works

1. Patient Upload: Patient uploads medical files (PDF, images) securely from the app.
2. Record Encryption: Each file is encrypted before storage.
3. Share via QR/Token: Patient selects a role (Doctor, Pharmacist, Diagnostics) and generates a time-limited QR code/token.
4. Provider Access: Healthcare provider scans the QR/token, gets only permitted access to the record.
5. Audit Log: Every access is logged with timestamp and user identity.
6. Patient Control: Patients can view logs and revoke access at any time.

---

## How to Install & Run

### Backend

1. Clone repository and `cd backend`
2. Create a `.env` file with:
   ```
   MONGO_URI=mongodb://localhost:27017/healthlock
   JWT_SECRET=your_secret_key
   ENCRYPTION_KEY=your_32byte_encryption_key
   ```
3. Install dependencies:  
   `npm install`
4. Start backend server:  
   `npm start`

### Android App

1. Open `android-app` in Android Studio
2. Sync Gradle to install dependencies
3. Update API base URL in your Kotlin code
4. Build & run on emulator or device

---

## What Can Be Improved

- UI/UX: More intuitive patient and doctor flows.
- File Storage: Use secure cloud storage for scalability.
- FHIR Integration: Deepen compatibility with healthcare standards.
- Notifications: Real-time alerts for access events.
- Access Revocation: More granular controls for expiring tokens.
- Security Audits: Pen-testing and compliance checks.

---

## Architecture

See [`docs/architecture.png`](docs/architecture.png) for system design.

---

**Built for hackathons. Focused on privacy, ease of use, and future scalability.**
