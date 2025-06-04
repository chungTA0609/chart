@echo off
echo.
echo 🚀 Setting up environment for Chart E-commerce Application...
echo.

(
echo # Chart E-commerce Application Environment Configuration
echo # Update these values with your actual configuration
echo.
echo # ================================
echo # Database Configuration
echo # ================================
echo DB_URL=jdbc:postgresql://localhost:5432/chart
echo DB_USERNAME=postgres
echo DB_PASSWORD=your_secure_database_password_here
echo.
echo # ================================
echo # JWT Configuration
echo # ================================
echo JWT_SECRET=supersecretkeySuperVjpPr0_%%^^&*
echo JWT_EXPIRATION=86400000
echo.
echo # ================================
echo # OAuth2 Social Login Configuration
echo # ================================
echo # Google OAuth2 ^(Get from: https://console.cloud.google.com/^)
echo GOOGLE_CLIENT_ID=your_google_client_id_here
echo GOOGLE_CLIENT_SECRET=your_google_client_secret_here
echo.
echo # Facebook OAuth2 ^(Get from: https://developers.facebook.com/^)
echo FACEBOOK_CLIENT_ID=your_facebook_client_id_here
echo FACEBOOK_CLIENT_SECRET=your_facebook_client_secret_here
echo.
echo # ================================
echo # Email Configuration
echo # ================================
echo MAIL_HOST=smtp.gmail.com
echo MAIL_PORT=587
echo MAIL_USERNAME=your_email@gmail.com
echo MAIL_PASSWORD=your_gmail_app_password_here
echo.
echo # ================================
echo # Apache Kafka Configuration
echo # ================================
echo KAFKA_BOOTSTRAP_SERVERS=localhost:9092
echo.
echo # ================================
echo # RabbitMQ Configuration
echo # ================================
echo RABBITMQ_HOST=localhost
echo RABBITMQ_PORT=5672
echo RABBITMQ_USERNAME=guest
echo RABBITMQ_PASSWORD=guest
echo.
echo # ================================
echo # Logging Configuration
echo # ================================
echo LOG_LEVEL_ROOT=INFO
echo LOG_LEVEL_APP=DEBUG
echo LOG_LEVEL_SECURITY=INFO
echo SHOW_SQL=false
) > .env

echo ✅ .env file created successfully!
echo.
echo 📝 Next steps:
echo 1. Edit the .env file with your actual values:
echo    - Update DB_PASSWORD with your PostgreSQL password
echo    - Update email credentials for notifications
echo    - Update OAuth2 credentials for social login ^(optional^)
echo.
echo 2. Make sure PostgreSQL is running
echo.
echo 3. Create the database:
echo    createdb chart
echo.
echo 4. Run the application:
echo    mvn spring-boot:run
echo.
echo 🔒 Security Note: The .env file is automatically ignored by git
echo.
echo 🎉 Environment setup complete!
echo.
pause 