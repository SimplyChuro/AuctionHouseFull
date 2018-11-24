import Service from '@ember/service';
import Cookies from 'ember-cli-js-cookie';
import ENV from 'auction-house/config/environment';
import $ from 'jquery';

export default Service.extend({
  authToken: Cookies.get('auth-token'),
  userID: Cookies.get('user-id'),

  login(mail, pass){
    var _this = this;
    $.ajax({
      url: ENV.HOST_URL+'/api/v1/login',
      type: 'POST',
      data: JSON.stringify({
        email: mail,
        password: pass
      }),
      contentType: 'application/json;charset=utf-8',
      dataType: 'json',
      success: function(data){
        Cookies.set('auth-token', data[0].authToken);
        Cookies.set('user-id', data[1].userID);
        _this.authToken = data.authToken;
        _this.userID = data.userID;
      }
    });
  },
  logout(){
    var _this = this;
    $.ajax({
      url: ENV.HOST_URL+'/api/v1/logout',
      type: 'POST',
      headers: {
        'X-AUTH-TOKEN': _this.authToken
      },
      contentType: 'application/text'
    });
    Cookies.remove('auth-token');
    Cookies.remove('user-id');
  },
  
});
