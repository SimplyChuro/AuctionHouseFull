import Route from '@ember/routing/route';
import { hash } from 'rsvp';

export default Route.extend({
  model(params) {
    return hash({
      user: this.store.findRecord('user', params.user_id, { reload: true })
    })
  },
 
  setupController(controller, model) {
    this._super(controller, model);
    this.controllerFor('account/admin/single-user').set('name', model.user.name);
    this.controllerFor('account/admin/single-user').set('surname', model.user.surname);
    this.controllerFor('account/admin/single-user').set('dateOfBirth', model.user.dateOfBirth);
    this.controllerFor('account/admin/single-user').set('phoneNumber', model.user.phoneNumber);
    this.controllerFor('account/admin/single-user').set('selectedOption', model.user.gender);
    this.controllerFor('account/admin/single-user').set('street', model.user.address.get('street'));
    this.controllerFor('account/admin/single-user').set('city', model.user.address.get('city'));
    this.controllerFor('account/admin/single-user').set('zipCode', model.user.address.get('zipCode'));
    this.controllerFor('account/admin/single-user').set('state', model.user.address.get('state'));
    this.controllerFor('account/admin/single-user').set('country', model.user.address.get('country'));
  },

  actions: { 
    willTransition: function() {
      this.controllerFor('account/admin/single-user').send('clearFields');
    }
  }
});
