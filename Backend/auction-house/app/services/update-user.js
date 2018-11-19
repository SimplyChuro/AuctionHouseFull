import Service from '@ember/service';
import ENV from 'auction-house/config/environment';
import $ from 'jquery';

export default Service.extend({
  session: Ember.inject.service(),
  update(gender, dateOB, phone, street, city, zipCode, state, country){
    $.ajax({
      url: ENV.HOST_URL+'/api/v1/users',
      type: 'PUT',
      headers: {
        'X-AUTH-TOKEN': this.get('session').authToken
      },
      data: JSON.stringify({
        user: {
          gender: gender,
          dateOfBirth: dateOB,
          phoneNumber: phone,
          address: {
            street: street,
            city: city,
            zipCode: zipCode,
            state: state,
            country: country
          }
        }
      }),
      contentType: 'application/json;charset=utf-8',
      dataType: 'json',
    });
  }
});
