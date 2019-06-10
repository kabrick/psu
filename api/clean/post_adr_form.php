<?php
include 'dbconfig.php';

$sex = mysqli_real_escape_string($conn, $_POST['sex']);
$outcome = mysqli_real_escape_string($conn, $_POST['outcome']);
$seriousness_of_reaction = mysqli_real_escape_string($conn, $_POST['seriousness_of_reaction']);
$patient_name = mysqli_real_escape_string($conn, $_POST['patient_name']);
$patient_number = mysqli_real_escape_string($conn, $_POST['patient_number']);
$age = mysqli_real_escape_string($conn, $_POST['age']);
$weight = mysqli_real_escape_string($conn, $_POST['weight']);
$health_facility = mysqli_real_escape_string($conn, $_POST['health_facility']);
$district = mysqli_real_escape_string($conn, $_POST['district']);
$last_menstrual_period = mysqli_real_escape_string($conn, $_POST['last_menstrual_period']);
$trimester = mysqli_real_escape_string($conn, $_POST['trimester']);
$suspected_drug_generic_name = mysqli_real_escape_string($conn, $_POST['suspected_drug_generic_name']);
$suspected_drug_brand_name = mysqli_real_escape_string($conn, $_POST['suspected_drug_brand_name']);
$suspected_drug_dose = mysqli_real_escape_string($conn, $_POST['suspected_drug_dose']);
$suspected_drug_date_started = mysqli_real_escape_string($conn, $_POST['suspected_drug_date_started']);
$suspected_drug_date_stopped = mysqli_real_escape_string($conn, $_POST['suspected_drug_date_stopped']);
$suspected_drug_prescribed_for = mysqli_real_escape_string($conn, $_POST['suspected_drug_prescribed_for']);
$suspected_drug_expiry_date = mysqli_real_escape_string($conn, $_POST['suspected_drug_expiry_date']);
$suspected_drug_batch_number = mysqli_real_escape_string($conn, $_POST['suspected_drug_batch_number']);
$reaction_and_treatment = mysqli_real_escape_string($conn, $_POST['reaction_and_treatment']);
$date_reaction_started = mysqli_real_escape_string($conn, $_POST['date_reaction_started']);
$date_reaction_stopped = mysqli_real_escape_string($conn, $_POST['date_reaction_stopped']);
$notification_date = mysqli_real_escape_string($conn, $_POST['notification_date']);
$other_drug_generic_name = mysqli_real_escape_string($conn, $_POST['other_drug_generic_name']);
$other_drug_brand_name = mysqli_real_escape_string($conn, $_POST['other_drug_brand_name']);
$other_drug_dosage = mysqli_real_escape_string($conn, $_POST['other_drug_dosage']);
$other_drug_date_started = mysqli_real_escape_string($conn, $_POST['other_drug_date_started']);
$other_drug_date_stopped = mysqli_real_escape_string($conn, $_POST['other_drug_date_stopped']);
$other_drug_indication = mysqli_real_escape_string($conn, $_POST['other_drug_indication']);
$relevant_lab_tests = mysqli_real_escape_string($conn, $_POST['relevant_lab_tests']);
$additional_relevant_information = mysqli_real_escape_string($conn, $_POST['additional_relevant_information']);
$reporter_name = mysqli_real_escape_string($conn, $_POST['reporter_name']);
$reporter_contacts = mysqli_real_escape_string($conn, $_POST['reporter_contacts']);
$reporter_date = mysqli_real_escape_string($conn, $_POST['reporter_date']);
$reporter_health_facility = mysqli_real_escape_string($conn, $_POST['reporter_health_facility']);
$submitted_by = mysqli_real_escape_string($conn, $_POST['submitted_by']);
$timestamp = mysqli_real_escape_string($conn, $_POST['timestamp']);

$sql = "INSERT INTO psu_adr_form (sex, outcome, seriousness_of_reaction, patient_name, patient_number, age, weight, health_facility, district, last_menstrual_period, trimester, suspected_drug_generic_name, suspected_drug_brand_name, suspected_drug_dose, suspected_drug_date_started, suspected_drug_date_stopped, suspected_drug_prescribed_for, suspected_drug_expiry_date, suspected_drug_batch_number, reaction_and_treatment, date_reaction_started, date_reaction_stopped, notification_date, other_drug_generic_name, other_drug_brand_name, other_drug_dosage, other_drug_date_started, other_drug_date_stopped, other_drug_indication, relevant_lab_tests, additional_relevant_information, reporter_name, reporter_contacts, reporter_date, reporter_health_facility, submitted_by, timestamp) VALUES ('$sex','$outcome','$seriousness_of_reaction','$patient_name','$patient_number','$age','$weight','$health_facility','$district','$last_menstrual_period','$trimester','$suspected_drug_generic_name','$suspected_drug_brand_name','$suspected_drug_dose','$suspected_drug_date_started','$suspected_drug_date_stopped','$suspected_drug_prescribed_for','$suspected_drug_expiry_date','$suspected_drug_batch_number','$reaction_and_treatment','$date_reaction_started','$date_reaction_stopped','$notification_date','$other_drug_generic_name','$other_drug_brand_name','$other_drug_dosage','$other_drug_date_started','$other_drug_date_stopped','$other_drug_indication','$relevant_lab_tests','$additional_relevant_information','$reporter_name','$reporter_contacts','$reporter_date','$reporter_health_facility','$submitted_by','$timestamp')";

if ($conn->query($sql) === TRUE) {
    echo $conn->insert_id;
} else {
    echo "0";
}
?>