import Controller from '@ember/controller';

export default Controller.extend({ 
  updateUser: Ember.inject.service(),
  selectedOption: null,
  actions: {
    setSelection: function(selected) {
      this.set('selectedOption', selected)
    },
    updateUser: function() {
      let selectedOption = this.get('selectedOption');
      this.get('updateUser').update(
        this.get('selectedOption'), 
        this.get('dateOfBirth'),
        this.get('phoneNumber'),
        this.get('street'),
        this.get('city'),
        this.get('zipCode'),
        this.get('state'),
        this.get('country') 
        ); 
    }
  }
});
