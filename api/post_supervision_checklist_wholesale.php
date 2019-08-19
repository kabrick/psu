<?php
include 'dbconfig.php';

$additional_notes = mysqli_real_escape_string($conn, $_POST['additional_notes']);
$pharmacy_name = mysqli_real_escape_string($conn, $_POST['pharmacy_name']);
$contact_name = mysqli_real_escape_string($conn, $_POST['contact_name']);
$contact = mysqli_real_escape_string($conn, $_POST['contact']);
$supervising_pharmacist = mysqli_real_escape_string($conn, $_POST['supervising_pharmacist']);
$reg_number = mysqli_real_escape_string($conn, $_POST['reg_number']);
$support_supervision_date = mysqli_real_escape_string($conn, $_POST['support_supervision_date']);
$location = mysqli_real_escape_string($conn, $_POST['location']);
$section_a_checklist = mysqli_real_escape_string($conn, $_POST['section_a_checklist']);
$section_b_checklist = mysqli_real_escape_string($conn, $_POST['section_b_checklist']);
$section_c_checklist = mysqli_real_escape_string($conn, $_POST['section_c_checklist']);
$section_d_checklist = mysqli_real_escape_string($conn, $_POST['section_d_checklist']);
$section_e_checklist = mysqli_real_escape_string($conn, $_POST['section_e_checklist']);
$section_f_checklist = mysqli_real_escape_string($conn, $_POST['section_f_checklist']);
$submitted_by = mysqli_real_escape_string($conn, $_POST['submitted_by']);
$timestamp = mysqli_real_escape_string($conn, $_POST['timestamp']);

$sql = "INSERT INTO psu_supervision_checklist_wholesale (additional_notes, pharmacy_name, contact_name, contact, supervising_pharmacist, reg_number, support_supervision_date, location, section_a_checklist, section_b_checklist, section_c_checklist, section_d_checklist, section_e_checklist, section_f_checklist, submitted_by, timestamp) VALUES ('$additional_notes','$pharmacy_name','$contact_name','$contact','$supervising_pharmacist','$reg_number','$support_supervision_date','$location','$section_a_checklist','$section_b_checklist','$section_c_checklist','$section_d_checklist','$section_e_checklist','$section_f_checklist','$submitted_by','$timestamp')";

if ($conn->query($sql) === TRUE) {
    echo $conn->insert_id;
} else {
    echo "0";
}
?>