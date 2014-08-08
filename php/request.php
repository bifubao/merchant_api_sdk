<?php
/**
 * request.php
 *
 * @author panlilu
 * @copyright bifubao.com
 * @since 2014-08
 */

require_once("bifubao_common.php");
global $bifubao_config;


$req = req_get_params();
$req['_pid_'] = $bifubao_config['pid'];
$req['_time_'] = time();
$to_sign_data = req_make_sign_data($req);
$checksum = md5($to_sign_data.$bifubao_config['key']);
$req['_checksum_'] = $checksum;

echo build_bifubao_request_form($req);
exit;