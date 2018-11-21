import Service from '@ember/service';
import Cookies from 'ember-cli-js-cookie';
import ENV from 'auction-house/config/environment';
import $ from 'jquery';

export default Service.extend({
  authToken: Cookies.get('auth-token'),
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
        Cookies.set('auth-token', data.authToken);
        _this.authToken = data.authToken;
      }
    });
  },
  logout(){
    $.ajax({
      url: ENV.HOST_URL+'/api/v1/logout',
      type: 'POST',
      headers: { 
        'X-AUTH-TOKEN': Cookies.get('auth-token')
      }
    });
    Cookies.remove('auth-token');
  },
  
});
