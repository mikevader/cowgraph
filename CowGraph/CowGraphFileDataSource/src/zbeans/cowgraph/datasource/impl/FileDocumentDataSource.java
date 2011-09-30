/*
 * Copyright (C) 2011 Michael M&uuml;hlebach <michael at anduin.ch>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package zbeans.cowgraph.datasource.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;
import zbeans.cowgraph.datasource.DocumentDataSource;
import zbeans.cowgraph.model.CowGraphDocument;
import zbeans.cowgraph.model.CowGraphVersion;

/**
 *
 * @author Michael M&uuml;hlebach <michael at anduin.ch>
 */
@ServiceProvider(service = DocumentDataSource.class)
public class FileDocumentDataSource implements DocumentDataSource {

    @Override
    public List<CowGraphDocument> getDocuments() {
        List<CowGraphDocument> list = new LinkedList<CowGraphDocument>();

        CowGraphDocument document = new CowGraphDocument();
        addVersion("v1.1", document);
        addVersion("v2.0", document);
        addVersion("v3.0", document);

        document.setName("MyFirstDocument");

        list.add(document);

        return list;
    }

    private CowGraphVersion addVersion(String name, CowGraphDocument document) {
        CowGraphVersion version = new CowGraphVersion(document);
        version.setDate(new Date());
        version.setName(name);
                document.add(version);

        return version;
    }

    @Override
    public zbeans.cowgraph.model.CowGraphDocument getDocument(String documentName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
