<?php
include 'dbconfig.php';

$id = $_GET['id'];

$responce = array();

$sql = "SELECT * FROM psu_adr_form WHERE id = '$id'";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

$responce['sex'] = $result_assoc['sex'];
$responce['outcome'] = $result_assoc['outcome'];
$responce['seriousness_of_reaction'] = $result_assoc['seriousness_of_reaction'];
$responce['patient_name'] = $result_assoc['patient_name'];
$responce['patient_number'] = $result_assoc['patient_number'];
$responce['age'] = $result_assoc['age'];
$responce['weight'] = $result_assoc['weight'];
$responce['health_facility'] = $result_assoc['health_facility'];
$responce['district'] = $result_assoc['district'];
$responce['last_menstrual_period'] = $result_assoc['last_menstrual_period'];
$responce['trimester'] = $result_assoc['trimester'];
$responce['suspected_drug_generic_name'] = $result_assoc['suspected_drug_generic_name'];
$responce['suspected_drug_brand_name'] = $result_assoc['suspected_drug_brand_name'];
$responce['suspected_drug_dose'] = $result_assoc['suspected_drug_dose'];
$responce['suspected_drug_date_started'] = $result_assoc['suspected_drug_date_started'];
$responce['suspected_drug_date_stopped'] = $result_assoc['suspected_drug_date_stopped'];
$responce['suspected_drug_prescribed_for'] = $result_assoc['suspected_drug_prescribed_for'];
$responce['suspected_drug_expiry_date'] = $result_assoc['suspected_drug_expiry_date'];
$responce['suspected_drug_batch_number'] = $result_assoc['suspected_drug_batch_number'];
$responce['reaction_and_treatment'] = $result_assoc['reaction_and_treatment'];
$responce['date_reaction_started'] = $result_assoc['date_reaction_started'];
$responce['date_reaction_stopped'] = $result_assoc['date_reaction_stopped'];
$responce['notification_date'] = $result_assoc['notification_date'];
$responce['other_drug_generic_name'] = $result_assoc['other_drug_generic_name'];
$responce['other_drug_brand_name'] = $result_assoc['other_drug_brand_name'];
$responce['other_drug_dosage'] = $result_assoc['other_drug_dosage'];
$responce['other_drug_date_started'] = $result_assoc['other_drug_date_started'];
$responce['other_drug_date_stopped'] = $result_assoc['other_drug_date_stopped'];
$responce['other_drug_indication'] = $result_assoc['other_drug_indication'];
$responce['relevant_lab_tests'] = $result_assoc['relevant_lab_tests'];
$responce['additional_relevant_information'] = $result_assoc['additional_relevant_information'];
$responce['reporter_name'] = $result_assoc['reporter_name'];
$responce['reporter_contacts'] = $result_assoc['reporter_contacts'];
$responce['reporter_date'] = $result_assoc['reporter_date'];
$responce['reporter_health_facility'] = $result_assoc['reporter_health_facility'];

echo json_encode($responce);

?>