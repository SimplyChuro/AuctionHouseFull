import { helper } from '@ember/component/helper';

export function getImageId(params/*, hash*/) {  
  return "preview-image-" + params;
}

export default helper(getImageId);
