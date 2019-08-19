<?php
include 'dbconfig.php';

$id = $_GET['id'];

$responce = array();

$sql = "SELECT pharmacy_id FROM psu_pharm_directors WHERE psu_id = '$id'";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

$pharmacy_id = $result_assoc['pharmacy_id'];

$sql2 = "SELECT update_by, pharmacy FROM psu_pharmacies WHERE id = '$pharmacy_id'";

$result2 = $conn->query($sql2);

$result_assoc2 = $result2->fetch_assoc();

$id = $result_assoc2['update_by'];

$sql1 = "SELECT name FROM psu_admin WHERE psu_id = '$id'";

$result1 = $conn->query($sql1);
$result_assoc1 = $result1->fetch_assoc();

$responce['pharmacist_name'] = $result_assoc1['name'];
$responce['pharmacy_name'] = $result_assoc2['pharmacy'];

echo json_encode($responce);

?>