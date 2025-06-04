# Chart - E-commerce Spring Boot Application

A comprehensive e-commerce platform built with Spring Boot 3.4.4, featuring user authentication, product management, shopping cart, order processing, and advanced notification systems.

## 🚀 Features

### Core E-commerce Features
- **User Management**: Registration, authentication with JWT and OAuth2 (Google/Facebook)
- **Product Catalog**: Products with variants, specifications, images, and reviews
- **Shopping Experience**: Shopping cart, wishlist, and checkout process
- **Order Management**: Order tracking and status updates
- **Address Management**: Multiple shipping addresses per user
- **Notifications**: Real-time notifications for order updates

### Advanced Features
- **Message Queuing**: Apache Kafka and RabbitMQ integration
- **Email Service**: Templated emails with Thymeleaf
- **Caching & Performance**: Optimized database queries
- **Security**: JWT authentication, role-based authorization, OAuth2 social login
- **API Documentation**: OpenAPI/Swagger integration
- **Error Handling**: Comprehensive global exception handling

## 🛠 Technology Stack

- **Backend**: Spring Boot 3.4.4 (Java 21)
- **Database**: PostgreSQL with Spring Data JPA
- **Security**: Spring Security with JWT tokens
- **Message Brokers**: Apache Kafka, RabbitMQ
- **Email**: Spring Mail with Thymeleaf templates
- **Documentation**: SpringDoc OpenAPI
- **Build Tool**: Maven
- **Other**: Lombok, MapStruct, Validation

## 🔧 Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL 14+
- Apache Kafka (optional, for notifications)
- RabbitMQ (optional, for message queuing)
- SMTP server (for email notifications)

## ⚙️ Configuration

### 1. Environment Variables

Create a `.env` file or set environment variables for sensitive configuration:

```bash
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/chart
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password

# JWT Configuration
JWT_SECRET=your_jwt_secret_key_must_be_at_least_32_characters_long_secure_key_123
JWT_EXPIRATION=86400000

# OAuth2 Configuration
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
FACEBOOK_CLIENT_ID=your_facebook_client_id
FACEBOOK_CLIENT_SECRET=your_facebook_client_secret

# Email Configuration
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587

# Optional: Message Broker Configuration
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest
```

### 2. Database Setup

```sql
-- Create database
CREATE DATABASE chart;

-- The application will automatically create tables on startup
-- with spring.jpa.hibernate.ddl-auto=update
```

### 3. OAuth2 Setup (Optional)

#### Google OAuth2:
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing
3. Enable Google+ API
4. Create OAuth2 credentials
5. Add authorized redirect URIs: `http://localhost:8080/login/oauth2/code/google`

#### Facebook OAuth2:
1. Go to [Facebook Developers](https://developers.facebook.com/)
2. Create a new app
3. Add Facebook Login product
4. Configure OAuth redirect URIs: `http://localhost:8080/login/oauth2/code/facebook`

## 🚀 Running the Application

### 1. Clone the Repository
```bash
git clone <repository-url>
cd chart
```

### 2. Set Environment Variables
```bash
# Copy example configuration
cp src/main/resources/application-example.properties application-local.properties
# Edit application-local.properties with your actual values
```

### 3. Build and Run
```bash
# Build the application
mvn clean install

# Run the application
mvn spring-boot:run

# Or run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

The application will be available at `http://localhost:8080`

## 📚 API Documentation

Once the application is running, access the API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🔐 Security Features

### Authentication & Authorization
- **JWT Tokens**: Secure stateless authentication
- **Role-based Access**: USER and ADMIN roles
- **OAuth2 Integration**: Social login with Google and Facebook
- **Password Encryption**: BCrypt hashing

### Security Configuration
- CORS enabled for frontend integration
- CSRF protection disabled for REST APIs
- Stateless session management
- Secure headers and HTTPS support

### API Security
```bash
# Public endpoints
POST /api/auth/register
POST /api/auth/login
GET  /api/products/**

# Authenticated endpoints (requires JWT token)
GET  /api/users/profile
POST /api/cart/add
GET  /api/orders

# Admin endpoints (requires ADMIN role)
GET  /api/admin/**
POST /api/admin/products
```

## 🛡️ Error Handling

The application includes comprehensive error handling:

### Supported Error Types
- **Validation Errors**: Field validation with detailed messages
- **Authentication Errors**: Invalid credentials, expired tokens
- **Authorization Errors**: Insufficient permissions
- **Business Logic Errors**: Stock issues, resource conflicts
- **Database Errors**: Constraint violations, connection issues
- **HTTP Errors**: Method not allowed, unsupported media type

### Error Response Format
```json
{
  "status": "ERROR",
  "message": "Validation failed",
  "data": null,
  "errors": [
    "email: Invalid email format",
    "firstName: First name is required"
  ]
}
```

## 🧪 API Usage Examples

### User Registration
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "securePassword123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

### User Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "securePassword123"
  }'
```

### Accessing Protected Endpoints
```bash
curl -X GET http://localhost:8080/api/users/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🔍 Monitoring & Debugging

### Logging Configuration
The application uses structured logging with different levels:

```properties
# Adjust log levels via environment variables
LOG_LEVEL_ROOT=INFO
LOG_LEVEL_APP=DEBUG
LOG_LEVEL_SECURITY=INFO
SHOW_SQL=false
```

### Health Checks
- Application health: `GET /actuator/health` (if Actuator is enabled)
- Database connectivity is verified on startup

## 🚧 Troubleshooting

### Common Issues

#### Database Connection
```bash
# Check PostgreSQL is running
sudo systemctl status postgresql

# Verify connection
psql -h localhost -U postgres -d chart
```

#### JWT Token Issues
- Ensure JWT_SECRET is at least 32 characters
- Check token expiration (default: 24 hours)
- Verify token format in Authorization header: `Bearer <token>`

#### Validation Errors
- Check request body format
- Ensure required fields are present
- Verify field length constraints

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/new-feature`
3. Commit changes: `git commit -am 'Add new feature'`
4. Push to branch: `git push origin feature/new-feature`
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For questions or support, please:
1. Check the API documentation
2. Review error messages and logs
3. Create an issue in the repository
4. Contact the development team

---

**Note**: This application is configured for development environments. For production deployment, ensure you:
- Use environment-specific configuration files
- Enable HTTPS and secure headers
- Configure proper database connection pooling
- Set up monitoring and logging aggregation
- Review and harden security settings 