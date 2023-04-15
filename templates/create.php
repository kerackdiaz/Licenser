<?php
require_once '../functions.php';
require_once '../LicenseController.php';

$licenseController = new LicenseController();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $data = [
        'user_name' => $_POST['user_name'],
        'project_name' => $_POST['project_name'],
        'project_url' => $_POST['project_url'],
        'start_date' => $_POST['start_date'],
        'end_date' => $_POST['end_date'],
        'email' => $_POST['email'],
        'license_type' => $_POST['license_type'],
        'redirect_url' => $_POST['redirect_url'],
        'is_lifetime' => isset($_POST['is_lifetime']) ? 1 : 0,
        'created_at' => date('Y-m-d H:i:s'),
        'updated_at' => date('Y-m-d H:i:s'),
    ];

    if ($licenseController->createProject($data)) {
        header('Location: ../index.php');
        exit();
    }
}

include 'header.php';
?>

<h2>Agregar proyecto</h2>
<form action="create.php" method="post">
<form action="create.php" method="post">
    <label for="user_name">Nombre del usuario:</label>
    <input type="text" name="user_name" id="user_name" required><br>

    <label for="project_name">Nombre del proyecto:</label>
    <input type="text" name="project_name" id="project_name" required><br>

    <label for="project_url">URL del proyecto:</label>
    <input type="text" name="project_url" id="project_url" required><br>

    <label for="start_date">Fecha de inicio:</label>
    <input type="date" name="start_date" id="start_date" required><br>

    <label for="end_date">Fecha de finalización:</label>
    <input type="date" name="end_date" id="end_date" required><br>

    <label for="email">Correo electrónico:</label>
    <input type="email" name="email" id="email" required><br>

    <label for="license_type">Tipo de licencia:</label>
    <select id="license_type" name="license_type">
        <option value="demo" <?= $project->license_type == 'demo' ? 'selected' : '' ?>>DEMO</option>
        <option value="vitalicia" <?= $project->license_type == 'vitalicia' ? 'selected' : '' ?>>Vitalicia</option>
        <option value="anual" <?= $project->license_type == 'anual' ? 'selected' : '' ?>>Anual</option>
    </select>

    <label for="redirect_url">URL de redireccionamiento:</label>
    <input type="text" name="redirect_url" id="redirect_url" required><br>

    <label for="is_lifetime">Licencia vitalicia:</label>
    <input type="checkbox" name="is_lifetime" id="is_lifetime"><br>

    <input type="submit" value="Agregar">
</form>

</form>

<?php include 'footer.php'; ?>
