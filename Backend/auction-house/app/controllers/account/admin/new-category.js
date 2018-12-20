import Controller from '@ember/controller';
import { isEmpty } from '@ember/utils';
import swal from 'sweetalert';

export default Controller.extend({

  hasError: null,
  
  nameHasError: null,
  nameErrorMessage: null,

  type: null,
  selectedCategory: null,

  customValidations: function(){
    var checker = true

    if(isEmpty(this.get('name')) || (this.get('name').length != 0 && this.get('name').trim().length == 0)) {
      this.set('nameHasError', true);
      this.set('nameErrorMessage', 'Category name can not be blank');
      checker = false;
    } else {
      if(this.get('name').length > 50){
        this.set('nameHasError', true);
        this.set('nameErrorMessage', 'The given name is way too big');
        checker = false;
      } else {
        this.set('nameHasError', false);
      }
    }

    return checker;
  },

  actions: {
    saveCategory: function() {
      var _this = this;

      if(this.customValidations()){
        let category = this.store.createRecord('category');
        
        category.set('name', this.get('name'));
        category.set('parent_id', this.get('selectedCategory'));
        category.save().then(function(){
          swal("Success!", "You have successfully created a category!", "success");
        }).catch(function(){
          
        });
      }
    },

    setCategoryType: function(type){
      this.set('type', type);
      if(type == 'main'){
        this.set('selectedCategory', null);
      }
    },

    setParentCategory: function(category){
      this.set('selectedCategory', category);
    },

    clearFields: function() {
      this.set('hasError', null);
      this.set('nameHasError', null);
      this.set('nameErrorMessage', null);
      this.set('type', null);
      this.set('selectedCategory', null);
    }
  }
});