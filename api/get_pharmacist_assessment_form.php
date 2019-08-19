<?php
include 'dbconfig.php';

$id = $_GET['id'];

$responce = array();

$sql = "SELECT * FROM psu_pharmacist_assessment WHERE id = '$id'";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

$pharmacy_id = $result_assoc['pharmacy_id'];

$sql2 = "SELECT pharmacy FROM psu_pharmacies WHERE id = '$pharmacy_id'";

$result2 = $conn->query($sql2);
$result_assoc2 = $result2->fetch_assoc();

$pharmacist_id = $result_assoc['pharmacist_id'];

$sql1 = "SELECT name FROM psu_admin WHERE psu_id = '$pharmacist_id'";

$result1 = $conn->query($sql1);
$result_assoc1 = $result1->fetch_assoc();

$responce['pharmacist_name'] = $result_assoc1['name'];
$responce['pharmacy_name'] = $result_assoc2['pharmacy'];
$responce['appraiser_name'] = $result_assoc['appraiser_name'];
$responce['appraiser_title'] = $result_assoc['appraiser_title'];
$responce['from_period'] = $result_assoc['from_period'];
$responce['to_period'] = $result_assoc['to_period'];
$responce['score_one'] = $result_assoc['score_one'];
$responce['score_two'] = $result_assoc['score_two'];
$responce['score_three'] = $result_assoc['score_three'];
$responce['score_four'] = $result_assoc['score_four'];
$responce['score_five'] = $result_assoc['score_five'];
$responce['remarks'] = $result_assoc['remarks'];

echo json_encode($responce);

?>