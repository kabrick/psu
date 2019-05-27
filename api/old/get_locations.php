<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql = "SELECT * FROM psu_pharm_cordinates WHERE psu_id = '$id'";

$result = $conn->query($sql);

$responce = array();

if ($result->num_rows > 0){
	if ($result->num_rows == 1){
		$result_assoc = $result->fetch_assoc();
		$responce['first_location'] = "1";
		$responce['second_location'] = "0";
		$responce['first_altitude'] = $result_assoc['altitude'];
		$responce['first_latitude'] = $result_assoc['latitude'];
		$responce['first_longitude'] = $result_assoc['longitude'];
	} else if ($result->num_rows == 2){
		$counter = 1;
		while($row = $result->fetch_assoc()) {
			//check if two locations are set
			if($counter == 1){
				$responce['first_location'] = "1";
				$responce['first_altitude'] = $row['altitude'];
				$responce['first_latitude'] = $row['latitude'];
				$responce['first_longitude'] = $row['longitude'];
			} else if ($counter == 2) {
				//second location has been set
				$responce['second_location'] = "1";
				$responce['second_altitude'] = $row['altitude'];
				$responce['second_latitude'] = $row['latitude'];
				$responce['second_longitude'] = $row['longitude'];
			}
			++$counter;
		}
	}
} else {
	$responce['first_location'] = "0";
	$responce['second_location'] = "0";
}

echo json_encode($responce);

?>