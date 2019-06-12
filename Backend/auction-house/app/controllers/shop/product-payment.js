import Controller from '@ember/controller';
import ENV from 'auction-house/config/environment';
import $ from 'jquery';
import swal from 'sweetalert';
import { inject as service } from '@ember/service';
import { isEmpty } from '@ember/utils';
import { computed } from '@ember/object';

export default Controller.extend({
  customSession: service(),
  stripev3: service(),

  token: null,

  options: {
    hidePostalCode: true,
    style: {
      base: {
        color: '#333',
        fontSize: '18px',
        background: '#FCFCFC'
      }
    }
  },

  sortedBids: computed('model.product.bids.[]', function() {
    return this.get('model.product.bids').sortBy('amount').reverse();
  }),

  actions : {

    async pay() {
      if(!isEmpty(this.get('token'))) {
        var _this = this;
        $.ajax({
          url: ENV.HOST_URL+'/api/v1/product/payment',
          type: 'POST',
          headers: {
            'X-AUTH-TOKEN': _this.get('customSession').getAuthToken()
          },
          data: JSON.stringify({  
            paymentToken: _this.get('token')
          }),
          contentType: 'application/json;charset=utf-8',
          dataType: 'json'
        }).then(function() {
          swal("Success!", "You have successfully posted your payment for "+_this.get('model.product').name+"!", "success");
        }).catch(function() {
          swal("Ooops!", "It would seem an error has occurred please try again.", "error");
        });
      }
      
    },

    onBlur: function(stripeElement) {
      let stripe = this.get('stripev3');
      stripe.createToken(stripeElement).then(({token}) => {
        console.log(token);
        this.set('token', token);
      });
    },

    clearFields: function() {
      this.set('token', null);
    }

  }
});
