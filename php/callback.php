<?php
/**
 * callback.php
 *
 * @author panlilu
 * @copyright bifubao.com
 * @since 2014-08
 */

if (empty($_POST['_request_check_']) || empty($_POST['_signature_']) ||
  empty($_POST['_request_id_'])) {
  echo "invalid request_check or signature or request_id.";exit;
}

require_once("bifubao_common.php");
global $bifubao_config;

// bifubao rsa public, production
$bifubao_pubkey = $bifubao_config['bifubao_pubkey'];

$pubkey_id = openssl_pkey_get_public($bifubao_pubkey);

$signature_sha1 = base64_decode($_POST['_signature_sha1_']);
// verify
if (openssl_verify(req_make_sign_data($_POST), $signature_sha1,
    $pubkey_id, OPENSSL_ALGO_SHA1) !== 1) {
  echo "openssl_verify failure(sha1)";exit;
}

$_order = json_decode($_POST['content'], true);


/* order中包含的信息
$order = array(
  // the order content
  'order_id'                    => $_order['order_id'],
  'order_hash_id'               => $_order['order_hash_id'],
  'external_order_id'           => $_order['external_order_id'],
  'handle_status'               => $_order['handle_status'],
  'external_info'               => $_order['external_info'],
  'display_name'                => $_order['display_name'],
  'display_desc'                => $_order['display_desc'],
  'pay_user_id'                 => $_order['pay_user_id'],
  'price_btc'                   => $_order['price_btc'],
  'price_cny'                   => $_order['price_cny'],
  'pay_btc'                     => $_order['pay_btc'],  // unit: satoshi
  'ratio_btc2cny'               => $_order['ratio_btc2cny'],
  'onchain_receive_btc_address' => $_order['onchain_receive_btc_address'],
  'order_receipt_id'            => $_order['order_receipt_id'],
  'creation_time'               => $_order['creation_time'],
  'last_modify_time'            => $_order['last_modify_time'],
);
*/

if ($_order['handle_status'] < 1000) {

  // ↓↓↓↓↓↓↓↓在这里实现当收到的比特币数量不正确时候的处理逻辑（只发生在非币付宝用户支付且支付金额不足时）↓↓↓↓↓↓↓↓
  // todo : handle the order when the recieved amount is not correct

  // ↑↑↑↑↑↑↑↑在这里实现当收到的比特币数量不正确时候的处理逻辑（只发生在非币付宝用户支付且支付金额不足时）↑↑↑↑↑↑↑↑

  exit;
}


// ↓↓↓↓↓↓↓↓↓↓在这里实现支付完成时的业务逻辑，请勿echo任何信息↓↓↓↓↓↓↓↓↓↓

// todo: run my process according to the callback
// run_my_process($_order);

// ↑↑↑↑↑↑↑↑↑↑在这里实现支付完成时的业务逻辑，请勿echo任何信息↑↑↑↑↑↑↑↑↑↑

// 需要注意: 比较返回的金额与商家数记录的订单的金额是否相等，只有相等的情况下才认为是交易成功.
//          订单完成后，币付宝会立刻发送 POST 请求至商家提供的 callback URL。如果请求失败币付宝将于10秒、20秒、40秒后再尝试三次请求，40秒后则不再尝试通知
//          在接收到支付结果通知后，判断是否已进行过业务逻辑处理，防止重复进行业务逻辑处理.



// 返回_request_check_通知币付宝请求成功，请勿改动
echo $_POST['_request_check_'];

exit;