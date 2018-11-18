import Controller from '@ember/controller';
import ENV from 'auction-house/config/environment';
import $ from 'jquery';

export default Controller.extend({ 


  selectedOption: null,
  actions: {
    setSelection: function(selected) {
      this.set('selectedOption', selected)
    },
    updateUser: function() {
      let selectedOption = this.get('selectedOption');
      $.ajax({
        url: 'http://localhost:9000/api/v1/users',
        type: 'PUT',
        headers: {
          'X-AUTH-TOKEN': ENV.USER_TOKEN
        },
        data: JSON.stringify({
          user: {
            gender: this.get('selectedOption'),
            dateOfBirth: this.get('dateOfBirth'),
            phoneNumber: this.get('phoneNumber'),
            address: {
              street: this.get('street'),
              city: this.get('city'),
              zipCode: this.get('zipCode'),
              state: this.get('state'),
              country: this.get('country')
            }
          }
        }),
        contentType: 'application/json;charset=utf-8',
        dataType: 'json',
      });
    }
  }
});
