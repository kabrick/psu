<?php
include 'dbconfig.php';

$id = $_GET['id'];
$psu_id = $_GET['psu_id'];
$start_date = $_GET['start_date'];
$end_date = $_GET['end_date'];

$responce = array();

$sql = "SELECT a.email, a.phone, a.name, p.pharmacy, a.psu_id FROM psu_admin a, psu_pharmacies p WHERE p.id = '$id' AND a.psu_id = p.update_by";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

//get the default info
$responce['name'] = $result_assoc['name'];
$responce['email'] = $result_assoc['email'];
$responce['phone'] = $result_assoc['phone'];
$responce['pharmacy'] = $result_assoc['pharmacy'];
$responce['pharmacist_id'] = $pharmacist_id = $result_assoc['psu_id'];

//get the total hours spent
$total_hours = 0;
$monday_hours = 0;
$tuesday_hours = 0;
$wednesday_hours = 0;
$thursday_hours = 0;
$friday_hours = 0;
$saturday_hours = 0;
$sunday_hours = 0;

$sql = "SELECT working_hours, day_id FROM psu_attendance_log WHERE psu_id = '$psu_id' AND pharmacy_id = '$id' AND time_out > '$start_date' AND time_out < '$end_date'";

$result = $conn->query($sql);

while($row = $result->fetch_assoc()) {
	$total_hours += $row['working_hours'];

	if($row['day_id'] == 1){
		$sunday_hours += $row['working_hours'];
	} else if ($row['day_id'] == 2){
		$monday_hours += $row['working_hours'];
	} else if ($row['day_id'] == 3){
		$tuesday_hours += $row['working_hours'];
	} else if ($row['day_id'] == 4){
		$wednesday_hours += $row['working_hours'];
	} else if ($row['day_id'] == 5){
		$thursday_hours += $row['working_hours'];
	} else if ($row['day_id'] == 6){
		$friday_hours += $row['working_hours'];
	} else if ($row['day_id'] == 7){
		$saturday_hours += $row['working_hours'];
	}
}

$responce['total_hours'] = $total_hours;
$responce['monday_hours'] = $monday_hours;
$responce['tuesday_hours'] = $tuesday_hours;
$responce['wednesday_hours'] = $wednesday_hours;
$responce['thursday_hours'] = $thursday_hours;
$responce['friday_hours'] = $friday_hours;
$responce['saturday_hours'] = $saturday_hours;
$responce['sunday_hours'] = $sunday_hours;

echo json_encode($responce);

?>