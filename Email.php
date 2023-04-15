<?php

require_once 'LicenseController.php';
require_once 'vendor/autoload.php'; 

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

class Email
{
    public static function sendEmail($to, $subject, $message)
    {
        $mail = new PHPMailer(true);

        try {
            // Configurar el servidor de correo
            $mail->SMTPDebug = 0;
            $mail->isSMTP();
            $mail->Host = 'smtp.example.com'; // Reemplaza esto con la dirección del servidor SMTP de tu proveedor de correo
            $mail->SMTPAuth = true;
            $mail->Username = 'username@example.com'; // Reemplaza esto con tu nombre de usuario del correo
            $mail->Password = 'yourpassword'; // Reemplaza esto con tu contraseña del correo
            $mail->SMTPSecure = 'tls';
            $mail->Port = 587;

            // Configurar la información del remitente y destinatario
            $mail->setFrom('no-reply@yourdomain.com', 'No Reply');
            $mail->addAddress($to);

            // Configurar el contenido del correo
            $mail->isHTML(true);
            $mail->Subject = $subject;
            $mail->Body = $message;

            // Enviar el correo
            $mail->send();
            return true;
        } catch (Exception $e) {
            // Opcional: manejar errores al enviar el correo
            // echo 'Message could not be sent. Mailer Error: ', $mail->ErrorInfo;
            return false;
        }
    }

    public static function sendProjectEmail($projectId, $subject, $message)
    {
        $licenseController = new LicenseController();
        $project = $licenseController->getProjectById($projectId);

        if ($project !== null) {
            $to = $project->email;
            return self::sendEmail($to, $subject, $message);
        } else {
            // El proyecto no fue encontrado, manejar el error como sea necesario
            return false;
        }
    }
}
