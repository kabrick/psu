<?php

function send_push_notification($title, $message) {
	$fields = array(
		"notification" => array(
				"title" => $title,
				"body" => $message
			),
		"to" => "/topics/all"
	);

	// Set POST variables
	$url = 'https://fcm.googleapis.com/fcm/send';

	$headers = array(
		'Authorization: key=AAAAoL1T1jo:APA91bGxWbqTJatZ3oam3_lxAjDmH8GY4F9m-PLMouHytUTgxyxMu2sb1Oq4Il6dLCjE3nw5ExY9tdXpPVYdCnWGEuCCIBZShKrA78zOaUghfg2ehKt1tc33LOG2c2BnErCN7PlCaTYQ',
		'Content-Type: application/json'
	);

	// Open connection
	$ch = curl_init();

	// Set the url, number of POST vars, POST data
	curl_setopt($ch, CURLOPT_URL, $url);

	curl_setopt($ch, CURLOPT_POST, true);
	curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

	// Disabling SSL Certificate support temporarily
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

	curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

	// Execute post
	$result = curl_exec($ch);
	if($result === FALSE){
		//die('Curl failed: ' . curl_error($ch));
	}

	// Close connection
	curl_close($ch);
}

?>