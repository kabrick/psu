<?php
include 'dbconfig.php';

$id = $_GET['id'];
$psu_id = $_GET['psu_id'];
$start_date = $_GET['start_date'];
$end_date = $_GET['end_date'];

$sql = "SELECT time_in, time_out, (time_out - time_in) AS duration FROM psu_attendance_log WHERE psu_id = '$psu_id' AND pharmacy_id = '$id'  AND time_out > '$start_date' AND time_out < '$end_date'";

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row[] = $result->fetch_assoc()) {
 		$json = json_encode($row);
 	}
} else {
	//
}

echo $json;

?>