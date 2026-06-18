# 🎓 CampusConnect

> A comprehensive full-stack social networking platform for academic communities, enabling students, alumni, and faculty to connect, collaborate, and grow together.

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Frontend: React](https://img.shields.io/badge/Frontend-React%2018-blue)](https://reactjs.org/)
[![Backend: Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot%203-brightgreen)](https://spring.io/projects/spring-boot)
[![Database: MySQL](https://img.shields.io/badge/Database-MySQL%208.0-orange)](https://www.mysql.com/)
[![Deployment: Render](https://img.shields.io/badge/Deployment-Render-7B3FF2)](https://render.com)

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Quick Start](#quick-start)
- [Installation](#installation)
- [Running Locally](#running-locally)
- [Deployment](#deployment)
- [Database Schema](#database-schema)
- [Contributing](#contributing)
- [License](#license)
- [Support](#support)

## 🎯 Overview

CampusConnect is a modern social networking platform specifically designed for academic institutions. It bridges the gap between current students, alumni, and faculty members by providing tools for:

- **Social Networking:** Create posts, like, comment, and follow other users
- **Professional Networking:** Connect with alumni and industry professionals
- **Career Services:** Job postings, applications, and tracking
- **Event Management:** Organize and attend campus events
- **Direct Messaging:** Communicate privately with other users
- **Admin Verification:** Secure account approval system for institutional control

## ✨ Features

### 👥 User Management
- **Role-Based Access Control:** Student, Alumni, Faculty, and Admin roles
- **Account Verification:** New users pending admin approval before access
- **User Profiles:** Comprehensive profiles with education and career information
- **Authentication:** Secure login/logout with session management
- **Profile Customization:** Bio, profile picture, and role-specific information

### 📱 Social Features
- **News Feed:** Personalized feed with posts from followed users
- **Posts:** Create, edit, delete posts with media support
- **Likes & Comments:** Engage with community content
- **Follow System:** Follow/unfollow users and manage follow requests
- **User Discovery:** Find and connect with other users

### 👨‍💼 Career Services
- **Job Board:** Alumni post job opportunities (Full-time, Part-time, Internship, Contract)
- **Job Applications:** Students apply with cover letter and resume
- **Application Tracking:** View and manage application status (Pending, Reviewed, Shortlisted, Rejected)
- **Notifications:** Real-time updates on job applications

### 📅 Event Management
- **Event Creation:** Faculty create and manage campus events
- **Event RSVP:** Students and alumni attend events
- **Attendee Management:** View event attendees and attendance lists
- **Event Filtering:** Filter events by category and date

### 💬 Messaging
- **Direct Messaging:** One-on-one conversations between users
- **Conversation Threads:** Organized conversation history
- **Unread Tracking:** Track unread messages
- **Real-time Updates:** Instant message delivery

### 🔔 Notifications
- **Multi-type Notifications:** Likes, comments, follows, job applications, events, etc.
- **Read/Unread Status:** Track notification status
- **In-app Notifications:** Real-time notification delivery
- **Notification History:** View past notifications

### 🛡️ Admin Dashboard
- **Account Verification:** Review and approve/reject new user registrations
- **Verification History:** View all verification requests and actions
- **User Management:** Monitor user activities and account status
- **Notes & Reasons:** Add notes for approvals or rejection reasons

## 🛠 Tech Stack

### Frontend
- **React.js 18** - UI library for building dynamic interfaces
- **Material-UI (MUI)** - Component library for professional UI design
- **React Router** - Client-side routing
- **Axios** - HTTP client for API communication
- **Context API** - State management
- **Vercel** - Deployment platform

### Backend
- **Spring Boot 3.5.7** - Java framework for building REST APIs
- **Spring Data JPA** - ORM for database operations
- **Hibernate** - Object-relational mapping
- **MySQL 8.0** - Relational database
- **Maven** - Build tool and dependency management
- **Render** - Deployment platform

### Database
- **MySQL 8.0** - Primary database
- **PostgreSQL** - Alternative for Render deployment
- **Docker** - Containerization for local development

### Development Tools
- **Git** - Version control
- **GitHub** - Repository hosting
- **Maven** - Backend build automation
- **npm** - Frontend package management
- **VS Code** - Code editor

## 🚀 Quick Start

### Prerequisites
- Node.js 16+ and npm
- Java 17 or higher
- Maven 3.8+
- MySQL 8.0 or PostgreSQL
- Docker (optional, for MySQL)

### Clone Repository
```bash
git clone https://github.com/YOUR_USERNAME/campusconnect.git
cd campusconnect
```

## 📦 Installation

### Backend Setup
```bash
cd campusconnect-backend

# Install dependencies
mvn install

# Build project
mvn clean build
```

### Frontend Setup
```bash
cd campusconnect-frontend

# Install dependencies
npm install

# Create environment file
echo "REACT_APP_API_URL=http://localhost:8080/api" > .env.local
```

## ▶️ Running Locally

### Start MySQL (Docker)
```bash
docker run --name campusconnect-mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=campusconnect \
  -p 3306:3306 \
  -d mysql:8.0
```

### Start Backend
```bash
cd campusconnect-backend
mvn spring-boot:run
```

Backend runs on: `http://localhost:8080`

### Start Frontend
```bash
cd campusconnect-frontend
npm start
```

Frontend runs on: `http://localhost:3000`

### Login Credentials
- **Admin:** 
  - Email: `admin@campusconnect.com`
  - Password: `admin123`

## 🌐 Deployment

CampusConnect is deployed on Render and Vercel:

- **Frontend:** https://campusconnect-frontend-6j8c.onrender.com
- **Backend:** https://campusconnect-1-83dn.onrender.com
For detailed deployment instructions, see [DEPLOYMENT.md](./docs/DEPLOYMENT.md)


## 📚 Documentation

### Quick Links
- [Architecture Overview](./docs/ARCHITECTURE.md) - System design and architecture
- [API Documentation](./docs/API_DOCUMENTATION.md) - REST API endpoints
- [Database Schema](./docs/DATABASE_SCHEMA.md) - Database structure
- [Setup Guide](./docs/SETUP.md) - Detailed installation guide
- [Features Guide](./docs/FEATURES.md) - Complete features documentation
- [Deployment Guide](./docs/DEPLOYMENT.md) - Deployment instructions

## 🔌 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login

### Users
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user profile

### Posts
- `GET /api/posts` - Get all posts
- `POST /api/posts` - Create post
- `PUT /api/posts/{id}` - Update post
- `DELETE /api/posts/{id}` - Delete post
- `POST /api/posts/{id}/like` - Like post
- `DELETE /api/posts/{id}/unlike` - Unlike post

### Jobs
- `GET /api/jobs` - Get all jobs
- `POST /api/jobs` - Create job (Alumni only)
- `POST /api/jobs/{id}/apply` - Apply for job
- `GET /api/jobs/{id}/applications` - Get applications
- `PUT /api/jobs/applications/{id}/status` - Update application status

### Events
- `GET /api/events` - Get all events
- `POST /api/events` - Create event (Faculty only)
- `POST /api/events/{id}/attend` - Attend event
- `DELETE /api/events/{id}/unattend` - Cancel event attendance

### Admin
- `GET /api/verification/pending` - Get pending verifications
- `POST /api/verification/{id}/approve` - Approve user
- `POST /api/verification/{id}/reject` - Reject user

For complete API documentation, see [API_DOCUMENTATION.md](./docs/API_DOCUMENTATION.md)

## 📊 Database Schema

### Core Tables
- **users** - User accounts and profiles
- **posts** - Social media posts
- **comments** - Post comments
- **likes** - Post and comment likes
- **follow_requests** - Follow relationships
- **messages** - Direct messages
- **events** - Campus events
- **event_attendees** - Event attendance
- **job_postings** - Job listings
- **job_applications** - Job applications
- **notifications** - User notifications
- **verification_requests** - Account verification

For detailed schema, see [DATABASE_SCHEMA.md](./docs/DATABASE_SCHEMA.md)

## 🤝 Contributing

Contributions are welcome! Please read [CONTRIBUTING.md](./CONTRIBUTING.md) for:
- Code of conduct
- How to report issues
- How to submit pull requests
- Development guidelines

### Quick Start for Contributors
1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see [LICENSE](./LICENSE) file for details.

## 📞 Support

### Getting Help
- **Issues:** Report bugs on [GitHub Issues](https://github.com/Avneesh-kun/CampusConnect/issues)
- **Discussions:** Join [GitHub Discussions](https://github.com/Avneesh-kun/CampusConnect/discussions)
- **Email:** avneeshk0606@gmail.com

## 👥 Authors

- **Avneesh Kumar** - Software Engineer
  - GitHub: [@avneesh](https://github.com/Avneesh-kun/CampusConnect)
  - Email: avneeshk0606@gmail.com

- **Khush** - Data Analyst
  - GitHub: [@khush](https://github.com/khush-84)
  - Email: kusht8056@gmail.com

- **Maaz Aftab** - Java Developer
  - GitHub: [@Maaz](https://github.com/maazaftab17)
  - Email: maazaftab17@gmail.com
 
- **Mayank Kumar Chauhan** - Software Engineer
  - GitHub: [@mayank](https://github.com/Mayank-rep1)
  - Email: mayankchauhan6888@gmail.com


## 🙏 Acknowledgments

- Material-UI team for amazing UI components
- Spring Boot community for excellent documentation
- React community for best practices

## 📈 Roadmap

### Upcoming Features
- [ ] Video calling for group discussions
- [ ] Advanced search and filtering
- [ ] Recommendation algorithm
- [ ] Mobile app (iOS/Android)
- [ ] File upload for resumes
- [ ] Email notifications
- [ ] Analytics dashboard
- [ ] Groups and communities

See [GitHub Projects](https://github.com/Avneesh-kun/CampusConnect/projects) for current development status.

---

**⭐ If you find this project useful, please consider giving it a star!**

Made with ❤️ by Avneesh Kumar & Team.
