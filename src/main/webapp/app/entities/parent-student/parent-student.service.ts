import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IParentStudent } from 'app/shared/model/parent-student.model';

type EntityResponseType = HttpResponse<IParentStudent>;
type EntityArrayResponseType = HttpResponse<IParentStudent[]>;

@Injectable({ providedIn: 'root' })
export class ParentStudentService {
    private resourceUrl = SERVER_API_URL + 'api/parent-students';

    constructor(private http: HttpClient) {}

    create(parentStudent: IParentStudent): Observable<EntityResponseType> {
        return this.http.post<IParentStudent>(this.resourceUrl, parentStudent, { observe: 'response' });
    }

    update(parentStudent: IParentStudent): Observable<EntityResponseType> {
        return this.http.put<IParentStudent>(this.resourceUrl, parentStudent, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IParentStudent>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IParentStudent[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
