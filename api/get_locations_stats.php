<?php
include 'dbconfig.php';

$responce = array();

$sql = "SELECT name FROM psu_admin WHERE type = 'pharmacists'";

$result = $conn->query($sql);

$responce['pharmacist_number'] = $result->num_rows;

$sql = "SELECT DISTINCT(update_by) FROM psu_pharmacies WHERE update_by IS NOT NULL";

$result = $conn->query($sql);

$responce['assigned_pharmacists'] = $result->num_rows;

$sql = "SELECT id FROM psu_pharmacies";

$result = $conn->query($sql);

$responce['pharmacies_number'] = $result->num_rows;

$sql = "SELECT id FROM psu_pharmacies WHERE location_set = 1";

$result = $conn->query($sql);

$responce['verified_pharmacies'] = $result->num_rows;

echo json_encode($responce);

?>