import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';
import { hash } from 'rsvp';

export default Route.extend({
  customSession: service(),

  model(){
    return hash({
      user: this.store.findRecord('user', this.get('customSession').getUserID(), { reload: true }),
    })
  },
  
  setupController(controller, model) {
    this._super(controller, model);
    this.controllerFor('account/profile').set('name', model.user.name);
    this.controllerFor('account/profile').set('surname', model.user.surname);
    this.controllerFor('account/profile').set('dateOfBirth', model.user.dateOfBirth);
    if(model.user.phoneNumber == null) {
      this.controllerFor('account/profile').set('phoneNumber', '');
    } else {
      this.controllerFor('account/profile').set('phoneNumber', model.user.phoneNumber);
    }
    this.controllerFor('account/profile').set('selectedOption', model.user.gender);
    this.controllerFor('account/profile').set('street', model.user.address.get('street'));
    this.controllerFor('account/profile').set('city', model.user.address.get('city'));
    this.controllerFor('account/profile').set('zipCode', model.user.address.get('zipCode'));
    this.controllerFor('account/profile').set('state', model.user.address.get('state'));
    this.controllerFor('account/profile').set('country', model.user.address.get('country'));
  },

  actions: {
    willTransition: function() {
      this.controllerFor('account/profile').send('clearFields');
    }
  }
});
