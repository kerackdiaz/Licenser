<?php

require_once 'functions.php';
require_once 'License.php';

class LicenseController
{
    public function __construct()
    {
        $this->conn = connectDB();
    }

    public function getAllProjects()
    {
        $sql = "SELECT * FROM projects";
        $stmt = $this->conn->query($sql);

        if ($stmt->rowCount() > 0) {
            $projects = [];
            while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
                $projects[] = new License($row);
            }
            return $projects;
        } else {
            return [];
        }
    }

    public function createProject($data)
    {
        $license = new License($data);
        $query = "INSERT INTO projects (user_name, project_name, project_url, license_key, start_date, end_date, email, license_type, redirect_url, is_lifetime, status, created_at, updated_at) VALUES (:user_name, :project_name, :project_url, :license_key, :start_date, :end_date, :email, :license_type, :redirect_url, :is_lifetime, :status, :created_at, :updated_at)";
        $stmt = $this->conn->prepare($query);
        $result = $stmt->execute($license->toArray());

        return $result;
    }

    public function updateProject($id, $data)
    {
        $license = new License($data);
        $query = "UPDATE projects SET user_name=:user_name, project_name=:project_name, project_url=:project_url, license_key=:license_key, start_date=:start_date, end_date=:end_date, email=:email, license_type=:license_type, redirect_url=:redirect_url, is_lifetime=:is_lifetime, updated_at=:updated_at WHERE id=:id";
        $stmt = $this->conn->prepare($query);
        $result = $stmt->execute(array_merge($license->toUpdateArray(), ['id' => $id]));
    
        return $result;
    }  

    public function toggleProjectStatus($id)
    {
        $query = "UPDATE projects SET status = !status WHERE id=:id";
        $stmt = $this->conn->prepare($query);
        $result = $stmt->execute([':id' => $id]);

        return $result;
    }

    public function filterProjectsByName($name)
    {
        $query = "SELECT * FROM projects WHERE project_name LIKE :name";
        $stmt = $this->conn->prepare($query);
        $stmt->execute([':name' => '%' . $name . '%']);
        $projects = $stmt->fetchAll(PDO::FETCH_ASSOC);

        $licenseList = [];
        foreach ($projects as $project) {
            $licenseList[] = new License($project);
        }

        return $licenseList;
    }

    public function getProjectById($id)
    {
        $sql = "SELECT * FROM projects WHERE id = :id";
        $stmt = $this->conn->prepare($sql);
        $stmt->execute([':id' => $id]);

        if ($stmt->rowCount() > 0) {
            return new License($stmt->fetch(PDO::FETCH_ASSOC));
        } else {
            return null;
        }
    }
    
    public function deleteProject($id)
{
    $query = "DELETE FROM projects WHERE id = :id";
    $stmt = $this->conn->prepare($query);
    $result = $stmt->execute([':id' => $id]);

    return $result;
}

public function getLicenseByKey($licenseKey)
{
    $sql = "SELECT * FROM projects WHERE license_key = :license_key";
    $stmt = $this->pdo->prepare($sql);
    $stmt->execute([':license_key' => $licenseKey]);

    return $stmt->fetchObject();
}

public function isLicenseValid($license)
{
    // Verificar si la licencia está activa
    if (!$license->status) {
        return false;
    }

    // Verificar si la licencia es vitalicia
    if ($license->is_lifetime) {
        return true;
    }

    // Verificar si la licencia no ha expirado
    $currentDate = new DateTime();
    $endDate = new DateTime($license->end_date);

    return $currentDate <= $endDate;
}
}
