<?php
require_once '../functions.php';
require_once '../LicenseController.php';

$licenseController = new LicenseController();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id = $_POST['id'];
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
        'is_active' => $_POST['is_active'],
    ];
    $updated = $licenseController->updateProject($id, $data);

    if ($updated) {
        header('Location: ../index.php');
        exit();
    } else {
        echo "Error al actualizar el proyecto.";
    }
}

if (isset($_GET['id'])) {
    $id = $_GET['id'];
    $project = $licenseController->getProjectById($id);

    if (!$project) {
        echo "Proyecto no encontrado.";
        exit();
    }
} else {
    echo "ID de proyecto no proporcionado.";
    exit();
}

include 'header.php';
?>


<h2>Editar proyecto</h2>
<form action="edit.php" method="post">
    <input type="hidden" name="id" value="<?= $project->id ?>">
    <label for="user_name">Nombre del usuario:</label>
    <input type="text" id="user_name" name="user_name" value="<?= $project->user_name ?>">
    
    <label for="project_name">Nombre del proyecto:</label>
    <input type="text" id="project_name" name="project_name" value="<?= $project->project_name ?>">
    
    <label for="project_url">URL del proyecto:</label>
    <input type="text" id="project_url" name="project_url" value="<?= $project->project_url ?>">

    <label for="start_date">Fecha de inicio:</label>
    <input type="date" id="start_date" name="start_date" value="<?= $project->start_date ?>">

    <label for="end_date">Fecha de finalización:</label>
    <input type="date" id="end_date" name="end_date" value="<?= $project->end_date ?>">

    <label for="email">Correo electrónico:</label>
    <input type="email" id="email" name="email" value="<?= $project->email ?>">

    <select id="license_type" name="license_type">
    <option value="demo" <?= $project->license_type == 'demo' ? 'selected' : '' ?>>DEMO</option>
    <option value="vitalicia" <?= $project->license_type == 'vitalicia' ? 'selected' : '' ?>>Vitalicia</option>
    <option value="anual" <?= $project->license_type == 'anual' ? 'selected' : '' ?>>Anual</option>
</select>

    <label for="redirect_url">URL de redirección:</label>
    <input type="text" id="redirect_url" name="redirect_url" value="<?= $project->redirect_url ?>">

    <label for="is_lifetime">Licencia de por vida:</label>
<input type="checkbox" id="is_lifetime" name="is_lifetime" <?= $project->is_lifetime ? 'checked' : '' ?>>

<select id="is_active" name="is_active">
    <option value="1" <?= $project->is_active ? 'selected' : '' ?>>Activo</option>
    <option value="0" <?= !$project->is_active ? 'selected' : '' ?>>Inactivo</option>
</select>

<input type="submit" value="Actualizar">
</form>

<?php include 'footer.php'; ?>
