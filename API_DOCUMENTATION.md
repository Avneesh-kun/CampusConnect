# CampusConnect API Documentation

Base URL: `https://campusconnect-1-83dn.onrender.com/api`

## Authentication Endpoints

### Register User
```http
POST /auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "role": "STUDENT",
  "rollNumber": "2024001",
  "department": "Computer Science"
}

Response: 200 OK
{
  "message": "Registration successful! Your account is pending admin verification.",
  "status": "PENDING"
}
```

### Login User
```http
POST /auth/login
Content-Type: application/json

{
  "email": "admin@campusconnect.com",
  "password": "admin123"
}

Response: 200 OK
{
  "id": 1,
  "email": "admin@campusconnect.com",
  "firstName": "Admin",
  "lastName": "User",
  "role": "ADMIN",
  "accountStatus": "APPROVED",
  "department": null,
  "rollNumber": null
}
```

## User Endpoints

### Get All Users
```http
GET /users

Response: 200 OK
[
  {
    "id": 1,
    "email": "user1@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "STUDENT",
    "department": "Computer Science"
  },
  ...
]
```

### Get User by ID
```http
GET /users/{id}

Response: 200 OK
{
  "id": 1,
  "email": "user1@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "STUDENT",
  "department": "Computer Science",
  "rollNumber": "2024001",
  "bio": "CS student interested in web development",
  "createdAt": "2026-01-15T10:30:00"
}
```

### Update User Profile
```http
PUT /users/{id}
Content-Type: application/json
Authorization: Bearer token

{
  "firstName": "John",
  "lastName": "Doe",
  "bio": "Updated bio",
  "department": "Computer Science"
}

Response: 200 OK
{
  "id": 1,
  "email": "user1@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "bio": "Updated bio",
  ...
}
```

## Post Endpoints

### Get All Posts
```http
GET /posts

Response: 200 OK
[
  {
    "id": 1,
    "content": "Great event today!",
    "author": {
      "id": 2,
      "firstName": "Jane",
      "lastName": "Smith"
    },
    "createdAt": "2026-02-15T14:30:00",
    "likesCount": 5,
    "commentsCount": 2
  },
  ...
]
```

### Create Post
```http
POST /posts
Content-Type: application/json
Authorization: Bearer token

{
  "content": "This is my first post on CampusConnect!",
  "userId": 1
}

Response: 201 Created
{
  "id": 1,
  "content": "This is my first post on CampusConnect!",
  "author": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe"
  },
  "createdAt": "2026-02-15T14:30:00"
}
```

### Like Post
```http
POST /posts/{postId}/like
Content-Type: application/json

{
  "userId": 1
}

Response: 200 OK
{
  "postId": 1,
  "userId": 1,
  "createdAt": "2026-02-15T14:35:00"
}
```

## Job Endpoints

### Get All Jobs
```http
GET /jobs?userId=1

Response: 200 OK
[
  {
    "id": 1,
    "title": "Software Engineer",
    "companyName": "TechCorp",
    "location": "San Francisco, CA",
    "jobType": "FULL_TIME",
    "experienceLevel": "JUNIOR",
    "salaryRange": "$80k - $120k",
    "postedBy": {
      "id": 5,
      "firstName": "Alice",
      "lastName": "Johnson"
    },
    "applicationsCount": 3,
    "createdAt": "2026-02-10T10:00:00"
  },
  ...
]
```

### Create Job (Alumni only)
```http
POST /jobs
Content-Type: application/json
Authorization: Bearer token

{
  "title": "Junior Developer",
  "description": "Looking for a junior developer...",
  "companyName": "StartupXYZ",
  "location": "New York, NY",
  "jobType": "FULL_TIME",
  "experienceLevel": "JUNIOR",
  "salaryRange": "$70k - $100k",
  "userId": 5
}

Response: 201 Created
{
  "id": 2,
  "title": "Junior Developer",
  "companyName": "StartupXYZ",
  ...
}
```

### Apply for Job
```http
POST /jobs/{jobId}/apply
Content-Type: application/json

{
  "userId": 1,
  "coverLetter": "I'm very interested in this position...",
  "resumeUrl": "https://example.com/resume.pdf"
}

Response: 201 Created
{
  "id": 1,
  "user": { "id": 1, "firstName": "John", "lastName": "Doe" },
  "jobPosting": { "id": 2, "title": "Junior Developer" },
  "status": "PENDING",
  "appliedAt": "2026-02-15T14:30:00"
}
```

### Update Application Status
```http
PUT /jobs/applications/{applicationId}/status
Content-Type: application/json

{
  "userId": 5,
  "status": "SHORTLISTED"
}

Response: 200 OK
{
  "id": 1,
  "status": "SHORTLISTED",
  "appliedAt": "2026-02-15T14:30:00"
}
```

## Event Endpoints

### Get All Events
```http
GET /events

Response: 200 OK
[
  {
    "id": 1,
    "title": "Tech Symposium",
    "description": "Annual technology symposium...",
    "location": "Campus Auditorium",
    "date": "2026-03-15T14:00:00",
    "category": "TECH",
    "createdBy": {
      "id": 3,
      "firstName": "Dr.",
      "lastName": "Smith"
    },
    "attendeesCount": 45
  },
  ...
]
```

### Create Event (Faculty only)
```http
POST /events
Content-Type: application/json

{
  "title": "Networking Event",
  "description": "Connect with alumni professionals...",
  "location": "Student Center",
  "date": "2026-03-20T18:00:00",
  "category": "NETWORKING",
  "userId": 3
}

Response: 201 Created
{
  "id": 2,
  "title": "Networking Event",
  ...
}
```

### Attend Event
```http
POST /events/{eventId}/attend
Content-Type: application/json

{
  "userId": 1
}

Response: 200 OK
{
  "id": 1,
  "eventId": 2,
  "userId": 1,
  "attendedAt": "2026-02-15T14:35:00"
}
```

## Notification Endpoints

### Get User Notifications
```http
GET /notifications/{userId}

Response: 200 OK
[
  {
    "id": 1,
    "user": { "id": 1 },
    "actor": { "id": 2, "firstName": "Jane" },
    "type": "LIKE",
    "message": "Jane liked your post",
    "isRead": false,
    "createdAt": "2026-02-15T14:30:00"
  },
  ...
]
```

### Mark Notification as Read
```http
PUT /notifications/{notificationId}/read

Response: 200 OK
```

## Admin Verification Endpoints

### Get Pending Verification Requests
```http
GET /verification/pending

Response: 200 OK
[
  {
    "id": 1,
    "user": {
      "id": 6,
      "email": "newuser@example.com",
      "firstName": "New",
      "lastName": "User",
      "role": "STUDENT"
    },
    "status": "PENDING",
    "requestedAt": "2026-02-14T10:00:00"
  },
  ...
]
```

### Approve User
```http
POST /verification/{requestId}/approve
Content-Type: application/json

{
  "adminId": 1,
  "notes": "Account verified. Welcome!"
}

Response: 200 OK
{
  "id": 1,
  "user": { "id": 6 },
  "status": "APPROVED",
  "reviewedAt": "2026-02-15T11:00:00"
}
```

### Reject User
```http
POST /verification/{requestId}/reject
Content-Type: application/json

{
  "adminId": 1,
  "reason": "Invalid academic credentials"
}

Response: 200 OK
{
  "id": 1,
  "user": { "id": 6 },
  "status": "REJECTED",
  "reviewedAt": "2026-02-15T11:00:00"
}
```

## Error Responses

### 400 Bad Request
```json
{
  "error": "Invalid request parameters"
}
```

### 401 Unauthorized
```json
{
  "error": "Your account is pending admin verification. Please wait for approval.",
  "status": "PENDING"
}
```

### 403 Forbidden
```json
{
  "error": "Access denied. Admin privileges required."
}
```

### 404 Not Found
```json
{
  "error": "User not found"
}
```

### 500 Internal Server Error
```json
{
  "error": "An unexpected error occurred"
}
```

---

For more details, see the source code documentation.
