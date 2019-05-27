<?php
include 'dbconfig.php';

$day = $_GET['day'];
$first = $_GET['first'];
$second = $_GET['second'];
$id = $_GET['id'];

$sql = "SELECT date, time_in, time_out FROM psu_attendance_log WHERE date BETWEEN '$first' AND '$second' AND psu_id = '$id' AND day_id = '$day'";

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