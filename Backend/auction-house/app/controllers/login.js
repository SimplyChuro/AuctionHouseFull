import Controller from '@ember/controller';
import ENV from 'auction-house/config/environment';
import $ from 'jquery';

export default Controller.extend({
  actions: {
    login: function() {
      $.ajax({
        url: 'http://localhost:9000/api/v1/login',
        type: 'POST',
        data: JSON.stringify({
          email: this.get('emailAddress'),
          password: this.get('password')
        }),
        contentType: 'application/json;charset=utf-8',
        dataType: 'json',
        success: function(data){
          ENV.USER_TOKEN = data.authToken;
        }
      }).then(() => {
        if(ENV.USER_TOKEN !== ''){
          this.transitionToRoute("home");
        } else {
          alert("Incorrect Loging Details");
        }
      });
    }
  }
});