# Contributing to CampusConnect

Thank you for your interest in contributing! We welcome contributions from everyone.

## Code of Conduct

This project and everyone participating in it is governed by our [Code of Conduct](./CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code.

## How to Contribute

### Reporting Bugs
Before creating a bug report, check the [issue list](https://github.com/Avneesh-kun/CampusConnect/issues) to avoid duplicates.

When creating a bug report, include:
- **Title:** Clear, descriptive title
- **Description:** Detailed description of the bug
- **Steps to Reproduce:** Step-by-step instructions
- **Expected Behavior:** What should happen
- **Actual Behavior:** What actually happens
- **Environment:** OS, Browser, Java version, Node version, etc.
- **Screenshots:** If applicable

### Suggesting Features
Feature suggestions are tracked as GitHub issues.

When creating a feature request, include:
- **Title:** Clear feature name
- **Description:** Detailed description of the feature
- **Use Case:** How this helps users
- **Implementation Notes:** Any implementation ideas (optional)

### Pull Requests

1. **Fork the Repository**
```bash
   git clone https://github.com/Avneesh-kun/CampusConnect.git
   cd campusconnect
```

2. **Create a Feature Branch**
```bash
   git checkout -b feature/amazing-feature
```

3. **Make Changes**
   - Follow the code style and conventions
   - Keep commits atomic and well-documented
   - Update documentation as needed

4. **Commit Changes**
```bash
   git commit -m 'Add some amazing feature'
```

5. **Push to Branch**
```bash
   git push origin feature/amazing-feature
```

6. **Open a Pull Request**
   - Provide clear description of changes
   - Reference any related issues
   - Ensure all tests pass

## Development Setup

### Prerequisites
- Node.js 16+
- Java 17+
- Maven 3.8+
- MySQL 8.0

### Backend Development
```bash
cd campusconnect-backend
mvn clean install
mvn spring-boot:run
```

### Frontend Development
```bash
cd campusconnect-frontend
npm install
npm start
```

## Code Style

### Java (Backend)
- Follow Google Java Style Guide
- Use meaningful variable names
- Add javadoc for public methods
- Maximum line length: 100 characters

### JavaScript (Frontend)
- Use ES6+ syntax
- Use meaningful component names
- Add JSDoc comments for functions
- Follow Airbnb JavaScript Style Guide

## Commit Messages

Use clear, descriptive commit messages:
[Type] Brief description
Detailed explanation of changes if needed.
Fixes #issue-number

Types: feat, fix, docs, style, refactor, test, chore

## Testing

- Backend: Run `mvn test`
- Frontend: Run `npm test`
- Include tests for new features
- Ensure all tests pass before submitting PR

## Documentation

- Update README.md if needed
- Add/update docs/ files for new features
- Include code comments for complex logic
- Update API documentation for new endpoints

## Questions?

Feel free to open an issue or start a discussion on GitHub.

---

Thank you for contributing! 🎉
