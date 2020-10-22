/*
 * Copyright (c) 2020 Mark A. Hunter
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.fhirfactory.pegacorn.ladon.dtcache.accessors;

import net.fhirfactory.pegacorn.ladon.dtcache.accessors.common.AccessorActionTypeEnum;
import net.fhirfactory.pegacorn.ladon.dtcache.accessors.common.AccessorBase;
import net.fhirfactory.pegacorn.ladon.dtcache.cache.DocumentReferenceCache;
import net.fhirfactory.pegacorn.ladon.dtcache.cache.GroupCache;
import net.fhirfactory.pegacorn.petasos.audit.model.PetasosParcelAuditTrailEntry;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.swing.text.Document;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class DocumentReferenceAccessor extends AccessorBase {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentReferenceAccessor.class);
    private static final String ACCESSOR_VERSION = "1.0.0";
    private static final String ACCESSOR_RESOURCE = "DocumentReference";

    @Inject
    DocumentReferenceCache docrefCache;

    public DocumentReferenceAccessor(){
        super();
    }

    public Class<DocumentReference> getResourceType() {
        return (DocumentReference.class);
    }

    public DocumentReference getResourceById(IdType theId) {
        LOG.debug(".getResourceById(): Entry, theId (IdType) --> {}", theId);
        PetasosParcelAuditTrailEntry currentTransaction = this.beginTransaction(theId, null, AccessorActionTypeEnum.GET);
        DocumentReference theResource = docrefCache.getDocumentReference(theId);
        if(theResource != null) {
            this.endTransaction(theId, theResource, AccessorActionTypeEnum.GET, true, currentTransaction);
        } else {
            this.endTransaction(theId, null, AccessorActionTypeEnum.GET, false, currentTransaction);
        }
        LOG.debug(".getResourceById(): Exit, DocumentReference retrieved --> {}", theResource);
        return (theResource);
    }

    public IdType addResource(DocumentReference newResource){
        LOG.debug(".addResource(): Entry, newResource (DocumentReference) --> {}", newResource);
        PetasosParcelAuditTrailEntry currentTransaction = this.beginTransaction(newResource.getIdElement(), newResource, AccessorActionTypeEnum.ADD);
        IdType addedResourceId = docrefCache.addDocumentReference(newResource);
        if(addedResourceId != null) {
            this.endTransaction(addedResourceId, newResource, AccessorActionTypeEnum.ADD, true, currentTransaction);
        } else {
            this.endTransaction(addedResourceId, null, AccessorActionTypeEnum.ADD, false, currentTransaction);
        }
        LOG.debug(".addResource(): Exit, addedResourceId (IdType) --> {}", addedResourceId);
        return(addedResourceId);
    }

    public IdType removeResource(DocumentReference resourceToRemove){
        LOG.debug(".removeResource(): Entry, resourceToRemove (DocumentReference) --> {}", resourceToRemove);
        PetasosParcelAuditTrailEntry currentTransaction = this.beginTransaction(resourceToRemove.getIdElement(), resourceToRemove, AccessorActionTypeEnum.DELETE);
        IdType removedResourceId = docrefCache.removeDocumentReference(resourceToRemove);
        if(removedResourceId != null) {
            this.endTransaction(removedResourceId, null, AccessorActionTypeEnum.DELETE, true, currentTransaction);
        } else {
            this.endTransaction(removedResourceId, null, AccessorActionTypeEnum.DELETE, false, currentTransaction);
        }
        LOG.debug(".removeResource(): Exit, removedResourceId (IdType) --> {}", removedResourceId);
        return(removedResourceId);
    }

    @Override
    protected String specifyAccessorName() {
        return (ACCESSOR_RESOURCE);
    }

    @Override
    protected String specifyVersion() {
        return (ACCESSOR_VERSION);
    }

    //
    // Search Functions
    //

    public List<DocumentReference> searchFor(CodeableConcept documentType, Date effectiveRangeStartDateTime, boolean startDateTimeIsInclusive, Date effectiveRangeEndDateTime, boolean endDateTimeIsInclusive  ){
        List<DocumentReference> docRefList = docrefCache.searchFor(documentType,effectiveRangeStartDateTime, startDateTimeIsInclusive, effectiveRangeEndDateTime, endDateTimeIsInclusive);
        return(docRefList);
    }
}
