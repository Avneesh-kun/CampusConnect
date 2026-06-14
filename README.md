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
- [API Documentation](#api-documentation)
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

## 📁 Project Structure
