#!/bin/bash

# Chart E-commerce Application - Environment Setup Script
# This script creates a .env file with all necessary environment variables

echo "🚀 Setting up environment for Chart E-commerce Application..."

# Create .env file
cat > .env << 'EOF'
# Chart E-commerce Application Environment Configuration
# Update these values with your actual configuration

# ================================
# Database Configuration
# ================================
DB_URL=jdbc:postgresql://localhost:5432/chart
DB_USERNAME=postgres
DB_PASSWORD=your_secure_database_password_here

# ================================
# JWT Configuration
# ================================
JWT_SECRET=supersecretkeySuperVjpPr0_%^&*
JWT_EXPIRATION=86400000

# ================================
# OAuth2 Social Login Configuration
# ================================
# Google OAuth2 (Get from: https://console.cloud.google.com/)
GOOGLE_CLIENT_ID=your_google_client_id_here
GOOGLE_CLIENT_SECRET=your_google_client_secret_here

# Facebook OAuth2 (Get from: https://developers.facebook.com/)
FACEBOOK_CLIENT_ID=your_facebook_client_id_here
FACEBOOK_CLIENT_SECRET=your_facebook_client_secret_here

# ================================
# Email Configuration
# ================================
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_gmail_app_password_here

# ================================
# Apache Kafka Configuration
# ================================
KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# ================================
# RabbitMQ Configuration
# ================================
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest

# ================================
# Logging Configuration
# ================================
LOG_LEVEL_ROOT=INFO
LOG_LEVEL_APP=DEBUG
LOG_LEVEL_SECURITY=INFO
SHOW_SQL=false
EOF

echo "✅ .env file created successfully!"
echo ""
echo "📝 Next steps:"
echo "1. Edit the .env file with your actual values:"
echo "   - Update DB_PASSWORD with your PostgreSQL password"
echo "   - Update email credentials for notifications"
echo "   - Update OAuth2 credentials for social login (optional)"
echo ""
echo "2. Make sure PostgreSQL is running:"
echo "   sudo systemctl start postgresql"
echo ""
echo "3. Create the database:"
echo "   createdb chart"
echo ""
echo "4. Run the application:"
echo "   mvn spring-boot:run"
echo ""
echo "🔒 Security Note: The .env file is automatically ignored by git"

# Make the .env file readable only by owner for security
chmod 600 .env

echo "🛡️  Set .env file permissions to 600 (owner read/write only)"
echo ""
echo "🎉 Environment setup complete!" 