import Route from '@ember/routing/route';

export default Route.extend({
  session: Ember.inject.service(),

  model(){
    return Ember.RSVP.hash({
      user: this.store.findRecord('user', this.get('session').userID)
    })
  }, 
  setupController(controller, model) {
    this._super(controller, model);
    this.controllerFor('account/profile').set('dateOfBirth', model.user.dateOfBirth);
    this.controllerFor('account/profile').set('phoneNumber', model.user.phoneNumber);
    this.controllerFor('account/profile').set('selectedOption', model.user.gender);
    this.controllerFor('account/profile').set('street', model.user.address.get('street'));
    this.controllerFor('account/profile').set('city', model.user.address.get('city'));
    this.controllerFor('account/profile').set('zipCode', model.user.address.get('zipCode'));
    this.controllerFor('account/profile').set('state', model.user.address.get('state'));
    this.controllerFor('account/profile').set('country', model.user.address.get('country'));
  }
});
