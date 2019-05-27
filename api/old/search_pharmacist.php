<?php
include 'dbconfig.php';

$id = $_GET['id'];
$pharmacy = $_GET['pharmacy'];

//date range
$first = $_GET['first'];
$second = $_GET['second'];

$sql1 = "SELECT pharmacy FROM psu_pharmacies WHERE update_by = '$id'";

$result1 = $conn->query($sql1);

$row1 = $result1->fetch_assoc();

$sql2 = "SELECT name FROM psu_admin WHERE psu_id = '$id'";

$result2 = $conn->query($sql2);

$row2 = $result2->fetch_assoc();

$responce = array();

$sql = "SELECT working_hours, day_id FROM psu_attendance_log WHERE date BETWEEN '$first' AND '$second' AND psu_id = '$id' AND pharmacy_id = '$pharmacy'";

$result = $conn->query($sql);

$working_hours = 0; 
$mon = 0; 
$tue = 0; 
$wed = 0; 
$thur = 0; 
$fri = 0; 
$sat = 0;
$sun = 0;

if ($result->num_rows > 0){
	$responce['result'] = "1";
	while($row = $result->fetch_assoc()) {
		switch ($row['day_id']) {
			case 1:
				$working_hours += $row['working_hours'];
				$sun += $row['working_hours'];
				break;
			
			case 2:
				$working_hours += $row['working_hours'];
				$mon += $row['working_hours'];
				break;
			
			case 3:
				$working_hours += $row['working_hours'];
				$tue += $row['working_hours'];
				break;
			
			case 4:
				$working_hours += $row['working_hours'];
				$wed += $row['working_hours'];
				break;
			
			case 5:
				$working_hours += $row['working_hours'];
				$thur += $row['working_hours'];
				break;
			
			case 6:
				$working_hours += $row['working_hours'];
				$fri += $row['working_hours'];
				break;
			
			case 7:
				$working_hours += $row['working_hours'];
				$sat += $row['working_hours'];
				break;
			
			default:
				//
				break;
		}
	}

	$responce['name'] = $row2['name'];
	$responce['pharmacy'] = $row1['pharmacy'];
	$responce['total'] = $working_hours;
	$responce['monday'] = $mon;
	$responce['tuesday'] = $tue;
	$responce['wednesday'] = $wed;
	$responce['thursday'] = $thur;
	$responce['friday'] = $fri;
	$responce['saturday'] = $sat;
	$responce['sunday'] = $sun;
} else {
	$responce['result'] = "0";
}

echo json_encode($responce);

?>