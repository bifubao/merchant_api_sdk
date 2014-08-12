<?php
/**
 * bifubao_common.php
 *
 * @author panlilu
 * @copyright bifubao.com
 * @since 2014-08
 */

require_once("bifubao_config.php");

//
// 获取参数
//
function req_get_params() {
  // 过滤参数用的白名单
  $key_white_list = ['external_order_id','price_btc','price_cny','display_name',
                     'display_desc','external_info','external_callback_url','external_redirect_url'];
  $arr = $_POST;
  unset($arr['_checksum_']);

  foreach ($arr as $_k => $_v) {
    if (in_array($_k,$key_white_list)){ // 白名单过滤参数
      $arr[$_k] = htmlspecialchars($_v); // 过滤特殊字符防跨站，请不要在参数内使用html相关特殊字符
    }
  }
  return $arr;
}

//
// 生成待签名字符串
//
function req_make_sign_data($arr=null) {
  if (empty($arr)) {
    $arr = $_POST;
  }
  unset($arr['_signature_']);
  unset($arr['_signature_sha1_']);
  unset($arr['_checksum_']);
  ksort($arr);
  $sign_str = '';
  foreach ($arr as $_k => $_v) {
    $sign_str .= $_k . $_v;
  }
  return $sign_str;
}

//
// 生成表单html
//
function build_bifubao_request_form($params) {
  global $bifubao_config;

  $sHtml = "<form id='bifubaosubmit' name='bifubaosubmit' action='".$bifubao_config['merchant_api_url']."' method='post'>";
  foreach($params as $_k => $_v) {
    $sHtml.= "<input type='hidden' name='".$_k."' value='".htmlspecialchars($_v)."'/>";
  }

  //submit按钮控件请不要含有name属性
  $sHtml = $sHtml."<input type='submit'/></form>";

  $sHtml = $sHtml."<script>document.forms['bifubaosubmit'].submit();</script>";

  return $sHtml;
}