import Controller from '@ember/controller';
import { isEmpty } from '@ember/utils'
import ENV from 'auction-house/config/environment';
import $ from 'jquery';
import swal from 'sweetalert';;

export default Controller.extend({

  queryParams: ['token'],
  token: null,

  actions: {
    async reactivateUser() {
      var _this = this;
      if(isEmpty(this.get('token'))) {
        _this.transitionToRoute('home');
      } else {
        $.ajax({
          url: ENV.HOST_URL+'/api/v1/activate',
          type: 'POST',
          headers: {
            'X-AUTH-TOKEN': _this.get('token')
          },
          contentType: 'application/text;charset=utf-8',
          dataType: 'text'
        }).catch(function() {
          _this.transitionToRoute('home');
        });
      }
    }
  }

});