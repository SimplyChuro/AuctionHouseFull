import Controller from '@ember/controller';

export default Controller.extend({ 
  selectedOption: null,

  actions: {
    setSelection: function(selected) {
      this.set('selectedOption', selected)
    },

    updateUser: function() {
      let selectedOption = this.get('selectedOption');
      var _this = this;
      let user = this.get('model.user');
      user.set('gender', this.get('selectedOption'));

      var bd = new Date(this.get('dateOfBirth'));
      bd.setMinutes(bd.getMinutes() + bd.getTimezoneOffset());
      user.set('dateOfBirth', bd);

      user.set('phoneNumber', this.get('phoneNumber'));
      user.set('address.street', this.get('street'));
      user.set('address.city', this.get('city'));
      user.set('address.zipCode', this.get('zipCode'));
      user.set('address.state', this.get('state'));
      user.set('address.country', this.get('country'));
      user.save().then(function(){

      }).catch(function(){
          _this.get('flashMessages').success('Updated Profile!');
      });
     
    }
  }
});
