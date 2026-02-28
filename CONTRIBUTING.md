# Contributing to VNStock Java KBS

Thank you for your interest in contributing to VNStock Java KBS! This document provides guidelines for contributing to the project.

## Getting Started

1. Fork the repository on GitHub
2. Clone your fork locally
3. Create a new branch for your feature or bugfix
4. Make your changes
5. Test your changes thoroughly
6. Submit a pull request

## Development Setup

### Prerequisites

- Java 25 or later
- Maven 3.9+
- Git

### Building the Project

```bash
# Clone your fork
git clone https://github.com/YOUR_USERNAME/vnstock-java-kbs.git
cd vnstock-java-kbs

# Build the project
mvn clean install -Dgpg.skip=true

# Run tests
mvn test
```

## Code Style

- Follow standard Java naming conventions
- Use meaningful variable and method names
- Add Javadoc comments for public APIs
- Keep methods focused and concise
- Write unit tests for new features

## Testing

- Write unit tests for all new features
- Ensure all tests pass before submitting PR
- Aim for high test coverage
- Use meaningful test names that describe what is being tested

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=VnstockKbsClientTest
```

## Submitting Changes

1. Commit your changes with clear, descriptive commit messages
2. Push your branch to your fork
3. Open a pull request against the main repository
4. Describe your changes in the PR description
5. Link any related issues

### Commit Message Format

```
<type>: <subject>

<body>

<footer>
```

Types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

Example:
```
feat: Add support for intraday trading data

- Implement new IntradayTrade model
- Add getIntradayTrades method to QuoteService
- Include unit tests for new functionality

Closes #123
```

## Reporting Issues

- Use the GitHub issue tracker
- Provide a clear description of the issue
- Include steps to reproduce
- Mention your Java version and OS
- Include relevant code snippets or error messages

## Questions?

Feel free to open an issue for questions or reach out to the maintainers.

## License

By contributing, you agree that your contributions will be licensed under the Apache License 2.0.
