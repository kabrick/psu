<?php
include 'dbconfig.php';

$id = $_GET['id'];

$responce = array();

$sql = "SELECT * FROM psu_supervision_checklist_wholesale WHERE id = '$id'";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

$responce['additional_notes'] = $result_assoc['additional_notes'];
$responce['pharmacy_name'] = $result_assoc['pharmacy_name'];
$responce['contact_name'] = $result_assoc['contact_name'];
$responce['contact'] = $result_assoc['contact'];
$responce['supervising_pharmacist'] = $result_assoc['supervising_pharmacist'];
$responce['reg_number'] = $result_assoc['reg_number'];
$responce['support_supervision_date'] = $result_assoc['support_supervision_date'];
$responce['location'] = $result_assoc['location'];
$responce['section_a_checklist'] = $result_assoc['section_a_checklist'];
$responce['section_b_checklist'] = $result_assoc['section_b_checklist'];

echo json_encode($responce);

?>