<?php
include 'dbconfig.php';

$sql = "SELECT * FROM psu_news WHERE approved = '1' ORDER BY id DESC";

$arr = array();
$counter = 0;

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row = $result->fetch_assoc()) {
		// get user name and photo using the psu_id

		$id = $row['author'];

		//get the author full name
		$sql1 = "SELECT name, photo FROM psu_admin WHERE psu_id = '$id'";

		$result1 = $conn->query($sql1);
		$result_assoc1 = $result1->fetch_assoc();

		$arr[$counter]['id'] = $row['id'];
		$arr[$counter]['title'] = $row['title'];
		$arr[$counter]['text'] = $row['text'];
		$arr[$counter]['timestamp'] = $row['timestamp'];
		$arr[$counter]['author'] = $result_assoc1['name'];
		$arr[$counter]['source'] = $row['source'];
		$arr[$counter]['photo'] = $result_assoc1['photo'];

		$counter++;
 	}
} else {
	//
}

echo json_encode($arr);

?>